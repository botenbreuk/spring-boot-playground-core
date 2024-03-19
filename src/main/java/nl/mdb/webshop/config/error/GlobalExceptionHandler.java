package nl.mdb.webshop.config.error;

import jakarta.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "SERVER.ERROR";
    public static final String SERVER_ENTITY_NOT_FOUND_ERROR = "SERVER.ENTITY_NOT_FOUND_ERROR";

    @ExceptionHandler({
            MissingPathVariableException.class,
            EntityNotFoundException.class,
            NoResourceFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFound(Exception ex) {
        return SERVER_ENTITY_NOT_FOUND_ERROR;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })
    public String handleOtherExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ERROR;
    }
}
