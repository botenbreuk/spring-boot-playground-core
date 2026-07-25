package nl.rdb.springbootplayground.architecture;

import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import nl.rdb.springbootplayground.architecture.utils.FieldTypeInspector;
import nl.rdb.springbootplayground.architecture.utils.ReturnTypeInspector;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;

public class ControllerArchitectureTest extends AbstractArchitectureTest {

    /**
     * Checks whether a class is either a Form (ends with *Form) or Result (ends with *Result)
     */
    public static final DescribedPredicate<JavaClass> areResultsOrForms = DescribedPredicate.describe(
            "are results or forms",
            clazz -> clazz.getName().endsWith("Result") || clazz.getName().endsWith("Form")
    );

    /**
     * Checks whether a method returns a class annotated with @Entity
     */
    ArchCondition<JavaMethod> returnEntities = condition(
            "return classes annotated with @Entity",
            (JavaMethod method, ConditionEvents events) -> new ReturnTypeInspector(importedClasses, method, events).inspect()
    );

    /**
     * Checks whether a class contains at least one field of a class annotated with @Entity
     */
    ArchCondition<JavaClass> containEntities = condition(
            "contain classes annotated with @Entity",
            (JavaClass clazz, ConditionEvents events) -> {
                for (JavaField field : clazz.getAllFields()) {
                    new FieldTypeInspector(importedClasses, field, events).inspect();
                }
            }
    );

    /**
     * Entities must only be used in the domain. Controllers and endpoints may only return derivates of the Entity,
     * classes ending with the name *Result. Returning an Entity directly exposes too much of the internals and offers
     * no way to control restricting what may be exposed.
     */
    @Test
    void methodsInControllersAndEndpointsMayNotReturnEntities() {
        methods()
                .that().areDeclaredInClassesThat(ARE_NOT_CONTROLLERS)
                .and().arePublic()
                .should(not(returnEntities))
                .check(importedClasses);
    }

    /**
     * Result or Form objects, ie classes with names ending in *Result or *Form, and which are used as value objects
     * between the client and the backend, should not contain entities. Returning an Entity directly i) exposes too
     * much of the internals and ii) opens the internal for uncontrolled mutations. In return it offers no way to
     * control restricting what may be exposed and mutated.
     */
    @Test
    void resultAndFormClassesShouldNotIncludeEntities() {
        classes()
                .that(areResultsOrForms)
                .should(not(containEntities))
                .check(importedClasses);
    }

    /**
     * All controllers must be annotated with @RestController. Using @Controller without @ResponseBody is inconsistent
     * with the REST API style used throughout the codebase, and plain @Controller is not configured in Spring Security.
     */
    @Test
    void controllersMustBeAnnotatedWithRestController() {
        classes().that()
                .haveSimpleNameEndingWith("Controller")
                .should().beAnnotatedWith(RestController.class)
                .check(importedClasses);
    }

    /**
     * Repositories are not decorated with @Transactional or security access settings, where Service methods are
     * decorated as such. Therefore, calling repositories directly in an endpoint or controller leave them exposed
     * to malicious parties.
     */
    @Test
    void noCallsToRepositoriesInEndpoints() {
        noClasses()
                .that(ARE_NOT_CONTROLLERS)
                .should().callMethodWhere(DescribedPredicate.describe(
                        "No Endpoint/Controller may directly call Repository methods",
                        input -> input.getTargetOwner().isMetaAnnotatedWith(Repository.class)))
                .check(importedClasses);
    }
}
