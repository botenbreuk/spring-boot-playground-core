package nl.rdb.springbootplayground.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import nl.rdb.springbootplayground.shared.security.annotation.SecurityUtil;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

class PreAuthorizeArchitectureTest extends AbstractArchitectureTest {

    private static final Pattern BEAN_REF_PATTERN = Pattern.compile("@(\\w+)\\.");

    /**
     * All @PreAuthorize expressions that reference a Spring bean (e.g. @beanName.method())
     * must reference a bean name that corresponds to a class annotated with @SecurityUtil in the codebase.
     * This prevents stale references to SecurityUtils beans that have been renamed or removed.
     */
    @Test
    void preAuthorizeSecurityUtilsBeanMustExist() {
        Set<String> registeredBeanNames = resolveSecurityUtilBeanNames();

        ArchCondition<JavaMethod> referencesExistingBeans = condition(
                "only reference existing @SecurityUtil beans in @PreAuthorize",
                (JavaMethod method, ConditionEvents events) -> {
                    String expression = method.getAnnotationOfType(PreAuthorize.class).value();
                    Matcher matcher = BEAN_REF_PATTERN.matcher(expression);
                    while (matcher.find()) {
                        String beanName = matcher.group(1);
                        if (!registeredBeanNames.contains(beanName)) {
                            events.add(SimpleConditionEvent.violated(method,
                                    "Method '%s' in '%s' references unknown @SecurityUtil bean '@%s' in @PreAuthorize(\"%s\")"
                                            .formatted(method.getName(), method.getOwner().getSimpleName(), beanName, expression)));
                        }
                    }
                }
        );

        methods().that()
                .areAnnotatedWith(PreAuthorize.class)
                .should(referencesExistingBeans)
                .check(importedClasses);
    }

    /**
     * All @PreAuthorize expressions on service methods must follow the convention
     * {@code @{ClassName}SecurityUtils.{methodName}(...)}, where the bean name ends in "SecurityUtils"
     * and the method name matches the annotated method's name.
     * This ensures a consistent and traceable security expression structure across all services.
     */
    @Test
    void preAuthorizeExpressionMustUseCorrectNamingConvention() {
        ArchCondition<JavaMethod> hasValidExpression = condition(
                "have the correct naming convention in a @PreAuthorize expression",
                (JavaMethod method, ConditionEvents events) -> {
                    String expression = method.getAnnotationOfType(PreAuthorize.class).value();
                    Pattern pattern = Pattern.compile("^@(\\w+(SecurityUtils)).(%s)\\((.*?)\\)$".formatted(method.getName()));
                    if (!pattern.matcher(expression).find()) {
                        events.add(SimpleConditionEvent.violated(method,
                                "Method '%s' in '%s' has @PreAuthorize with incorrect @SecurityUtils convention \"%s\"."
                                        .formatted(method.getName(), method.getOwner().getSimpleName(), expression)));
                    }
                }
        );

        methods().that()
                .areAnnotatedWith(PreAuthorize.class)
                .and(IS_IN_SERVICE)
                .should(hasValidExpression)
                .check(importedClasses);
    }

    private Set<String> resolveSecurityUtilBeanNames() {
        return importedClasses.stream()
                .filter(clazz -> clazz.isAnnotatedWith(SecurityUtil.class))
                .map(this::resolveBeanName)
                .collect(Collectors.toSet());
    }

    private String resolveBeanName(JavaClass clazz) {
        String value = clazz.getAnnotationOfType(SecurityUtil.class).value();
        if (!value.isEmpty()) {
            return value;
        }
        String simpleName = clazz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
