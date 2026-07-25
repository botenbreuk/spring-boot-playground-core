package nl.rdb.springbootplayground.architecture;

import java.util.Arrays;
import java.util.function.BiConsumer;

import lombok.extern.slf4j.Slf4j;
import nl.rdb.springbootplayground.Application;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.domain.properties.HasAnnotations;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;

@Slf4j
public abstract class AbstractArchitectureTest {

    /**
     * Checks whether a class is NOT annotated with @Service
     */
    protected static final DescribedPredicate<JavaClass> ARE_NOT_SERVICES = DescribedPredicate.describe(
            "are not services",
            clazz -> !clazz.isAnnotatedWith(Service.class)
    );

    /**
     * Checks whether a class is annotated with @Controller
     */
    public static final DescribedPredicate<JavaClass> ARE_NOT_CONTROLLERS = DescribedPredicate.describe(
            "are controllers",
            clazz -> clazz.isAnnotatedWith(RestController.class) || clazz.isAnnotatedWith(Controller.class)
    );

    public static final DescribedPredicate<JavaMethod> IS_IN_SERVICE = DescribedPredicate.describe(
            "is inside service",
            javaMethod -> javaMethod.getOwner().isAnnotatedWith(Service.class)
    );

    protected static JavaClasses importedClasses;
    protected static JavaClasses importedClassesWithTest;

    @BeforeAll
    public static void beforeAll() {
        if (importedClasses == null) {
            importedClasses = new ClassFileImporter().withImportOption(
                    new ImportOption.DoNotIncludeTests()).importPackages(Application.class.getPackageName());
        }

        if (importedClassesWithTest == null) {
            importedClassesWithTest = new ClassFileImporter().importPackages(Application.class.getPackageName());
        }
    }

    protected <T extends CanBeAnnotated & HasName & HasAnnotations<T>> DescribedPredicate<T> excludedFor(ViolationType violation) {
        return DescribedPredicate.describe(
                "excluded for violation '%s'".formatted(violation),
                input -> {
                    if (!input.isAnnotatedWith(ArchIgnore.class)) {
                        return false;
                    }

                    String name = input.getName();

                    ArchIgnore archIgnore = input.getAnnotationOfType(ArchIgnore.class);
                    if (Arrays.asList(archIgnore.violation()).contains(violation) && Arrays.asList(archIgnore.value()).contains(violation)) {
                        return false;
                    }

                    String reason = archIgnore.reason();
                    if (StringUtils.isBlank(reason)) {
                        log.info("Violation '{}' is excluded for '{}' but no reason was given.", violation, name);
                    } else {
                        log.info("Violation '{}' is excluded for '{}' because '{}'.", violation, name, reason);
                    }

                    return true;
                }
        );
    }

    protected static <T> ArchCondition<T> condition(String description, BiConsumer<T, ConditionEvents> check) {
        return new ArchCondition<>(description) {
            @Override
            public void check(T item, ConditionEvents events) {
                check.accept(item, events);
            }
        };
    }
}