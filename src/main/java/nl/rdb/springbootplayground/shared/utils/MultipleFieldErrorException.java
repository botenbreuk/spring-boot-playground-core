package nl.rdb.springbootplayground.shared.utils;

import java.util.List;

import lombok.Getter;

import org.springframework.validation.FieldError;

@Getter
public class MultipleFieldErrorException extends RuntimeException {

    private final List<FieldError> fieldErrors;

    public MultipleFieldErrorException(String message, List<FieldError> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}
