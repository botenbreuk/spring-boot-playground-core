package nl.rdb.springbootplayground.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

import nl.rdb.springbootplayground.email.EmailService;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;

class ServiceArchitectureTest extends AbstractArchitectureTest {

    /**
     * Every concrete service class (non-abstract) must be annotated with either @Service or @EmailService.
     * Unannotated service classes are not managed by Spring and won't have @Transactional or security applied.
     */
    @Test
    void concreteServiceClassesMustBeAnnotatedWithServiceOrEmailService() {
        DescribedPredicate<JavaClass> areNotAbstract = DescribedPredicate.describe("are not abstract",
                clazz -> !clazz.getModifiers().contains(JavaModifier.ABSTRACT));
        classes().that()
                .haveSimpleNameEndingWith("Service")
                .and(areNotAbstract)
                .and().areNotAnnotatedWith(EmailService.class)
                .should().beAnnotatedWith(Service.class)
                .check(importedClasses);
    }

    /**
     * Classes annotated with @EmailService must follow the naming convention of ending with "EmailService".
     * This ensures discoverability and consistency across the async email layer.
     */
    @Test
    void emailServiceClassesMustFollowNamingConvention() {
        classes().that()
                .areAnnotatedWith(EmailService.class)
                .should().haveSimpleNameEndingWith("EmailService")
                .check(importedClasses);
    }

    /**
     * All Services must be @Transactional, meaning that they will wrap any public call in a try-catch block with
     * rollback (failure) and commit (success) settings. Repository calls that are made within the service make use
     * of the same transaction. When services are not made @Transactional, they have uncertain transaction boundaries,
     * making the system unrealiable.
     */
    @Test
    void ensureServiceIsTransactional() {
        classes().that()
                .areAnnotatedWith(Service.class)
                .and(not(excludedFor(ViolationType.SERVICE_TRANSACTIONAL)))
                .should().beAnnotatedWith(Transactional.class)
                .check(importedClasses);
    }

    /**
     * Only the service layer keeps track of the Service Access Rules. The meta-annotation may not be applied to non-
     * services, which causes the kind of error that is extremely misleading, because all access rules are expected
     * only at the service level.
     */
    @Test
    void methodsInNonServicesMayNotHavePreAuthorize() {
        noMethods().that()
                .areDeclaredInClassesThat(ARE_NOT_SERVICES.and(not(ARE_NOT_CONTROLLERS))
                        .and(not(excludedFor(ViolationType.PUBLIC_PRE_POST_AUTHORIZE))))
                .should().beMetaAnnotatedWith(PreAuthorize.class)
                .check(importedClasses);
    }

    /**
     * Every public service method must be annotated with a ServiceAccessRule annotation, such as e.g. AccessForEveryone,
     * AccessForOwnerOfInitiative or AccessOnlyForOtherServices. The reason for having this architecture check is that
     * all calls must be explicitly given a security access setting, so that no public service methods get accidentally
     * forgotten and being unintentionally wide open for access to malicious parties.
     */
    @Test
    void allPublicServiceMethodsMustHavePreAuthorize() {
        methods().that()
                .arePublic()
                .and().areDeclaredInClassesThat().areAnnotatedWith(Service.class)
                .and(not(excludedFor(ViolationType.PUBLIC_PRE_POST_AUTHORIZE)))
                .should().beMetaAnnotatedWith(PreAuthorize.class).orShould().beMetaAnnotatedWith(PostAuthorize.class)
                .check(importedClasses);
    }
}
