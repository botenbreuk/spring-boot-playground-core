package nl.rdb.springbootplayground.shared.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;

public final class MultiFieldValidator {

    private final List<ValidationRule> rules = new ArrayList<>();
    private final Map<String, List<ValidationRule>> chainRules = new HashMap<>();
    private final String objectName;

    private String message;

    public MultiFieldValidator(String objectName) {
        this.objectName = objectName;
    }

    public MultiFieldValidator(Class<?> obj) {
        this.objectName = obj.getSimpleName();
    }

    public MultiFieldValidator addValidation(String field, UnaryOperator<ValidationRuleBuilder> validationRule) {
        ValidationRule rule = validationRule.apply(new ValidationRuleBuilder(field)).build();
        this.rules.add(rule);
        return this;
    }

    public MultiFieldValidator addValidationChain(String field, UnaryOperator<ValidationRuleChainBuilder> validationRules) {
        this.chainRules.putIfAbsent(field, validationRules.apply(new ValidationRuleChainBuilder(field)).build());
        return this;
    }

    public void validate() {
        if (message == null) {
            throw new NotAllFieldsAreSetException();
        }

        List<FieldError> errors = new ArrayList<>();
        List<FieldError> fieldErrorBodies = rules.stream()
                .filter(rule -> rule.check().get())
                .map(rule -> new FieldError(this.objectName, rule.field(), rule.message()))
                .toList();
        List<FieldError> chainErrors = chainRules.values().stream()
                .map(valRules -> {
                    ValidationRule rule = valRules.stream()
                            .filter(r -> r.check().get())
                            .findFirst()
                            .orElse(null);
                    if (rule == null) {
                        return null;
                    }

                    return new FieldError(this.objectName, rule.field(), rule.message());
                })
                .filter(Objects::nonNull)
                .toList();

        errors.addAll(fieldErrorBodies);
        errors.addAll(chainErrors);

        if (!errors.isEmpty()) {
            String test = errors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("\n", " - ", ""));
            throw new MultipleFieldErrorException("%s:%n %s".formatted(message, test), errors);
        }
    }

    public MultiFieldValidator message(String message) {
        this.message = message;
        return this;
    }

    public static class ValidationRuleChainBuilder {

        private final List<ValidationRule> checks = new ArrayList<>();
        private final String field;

        public ValidationRuleChainBuilder(@NotNull String field) {
            this.field = field;
        }

        public ValidationRuleChainBuilder validation(Supplier<Boolean> check, String message) {
            if (check == null || message == null) {
                throw new NotAllFieldsAreSetException();
            }

            this.checks.add(new ValidationRule(check, field, message));
            return this;
        }

        private List<ValidationRule> build() {
            return this.checks;
        }
    }

    public static class ValidationRuleBuilder {

        private final String field;

        private Supplier<Boolean> check;
        private String message;

        public ValidationRuleBuilder(@NotNull String field) {
            this.field = field;
        }

        public ValidationRuleBuilder validation(Supplier<Boolean> check, String message) {
            this.check = check;
            this.message = message;
            return this;
        }

        private ValidationRule build() {
            if (check == null || message == null) {
                throw new NotAllFieldsAreSetException();
            }

            return new ValidationRule(check, field, message);
        }
    }

    public static class NotAllFieldsAreSetException extends RuntimeException {

        public NotAllFieldsAreSetException() {
            super("Not all fields are set.");
        }
    }

    public record ValidationRule(Supplier<Boolean> check, String field, String message) {}
}
