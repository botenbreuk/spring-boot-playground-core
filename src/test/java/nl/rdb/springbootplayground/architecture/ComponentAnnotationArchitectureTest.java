package nl.rdb.springbootplayground.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import jakarta.persistence.Entity;

import nl.rdb.springbootplayground.shared.AbstractEntity;
import nl.rdb.springbootplayground.shared.security.annotation.SecurityUtil;

import org.hibernate.annotations.Immutable;
import org.junit.jupiter.api.Test;

class ComponentAnnotationArchitectureTest extends AbstractArchitectureTest {

    /**
     * All classes annotated with @SecurityUtil must end their name with "SecurityUtils". This enforces
     * a consistent naming convention for security utility components across all modules.
     */
    @Test
    void securityUtilAnnotatedClassesMustFollowNamingConvention() {
        classes().that()
                .areAnnotatedWith(SecurityUtil.class)
                .and(not(excludedFor(ViolationType.SECURITY_UTIL_NAMING)))
                .should().haveSimpleNameEndingWith("SecurityUtils")
                .check(importedClasses);
    }

    /**
     * All classes whose name ends with "SecurityUtils" (except the shared SecurityUtils utility class)
     * must be annotated with @SecurityUtil. This ensures security util components are properly registered
     * as Spring beans with the correct stereotype.
     */
    @Test
    void classesNamedSecurityUtilsMustBeAnnotatedWithSecurityUtil() {
        classes().that()
                .haveSimpleNameEndingWith("SecurityUtils")
                .and().doNotHaveSimpleName("SecurityUtils")
                .should().beAnnotatedWith(SecurityUtil.class)
                .check(importedClasses);
    }

    /**
     * All JPA entities must extend AbstractEntity. AbstractEntity provides the shared identity strategy
     * (@GeneratedValue), implements Persistable<Long>, and applies @DatabaseConstrained. Entities that
     * bypass it risk inconsistent ID generation and missing constraint validation.
     */
    @Test
    void entitiesMustExtendAbstractEntity() {
        classes()
                .that().areAnnotatedWith(Entity.class)
                .and().areNotAnnotatedWith(Immutable.class)
                .and(not(excludedFor(ViolationType.ENTITY_BASE_CLASS)))
                .should().beAssignableTo(AbstractEntity.class)
                .check(importedClasses);
    }
}
