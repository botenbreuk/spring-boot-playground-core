package nl.rdb.springbootplayground.config.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import nl.rdb.springbootplayground.config.error.GenericErrorHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class RestAccessDeniedHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    private static final String SERVER_AUTHENTICATE_ERROR = "SERVER.AUTHENTICATE_ERROR";
    private static final String SERVER_SESSION_TIMEOUT_ERROR = "SERVER.SESSION_TIMEOUT_ERROR";
    private static final String SERVER_ACCESS_DENIED_ERROR = "SERVER.ACCESS_DENIED_ERROR";

    private final GenericErrorHandler errorHandler;

    public RestAccessDeniedHandler(GenericErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Handles CSRF failures
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        if (ex instanceof MissingCsrfTokenException) {
            errorHandler.respond(response, UNAUTHORIZED, SERVER_SESSION_TIMEOUT_ERROR);
        } else {
            errorHandler.respond(response, FORBIDDEN, SERVER_ACCESS_DENIED_ERROR);
        }
    }

    /**
     * Handles authentication exception when trying to reach a restricted URL
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        errorHandler.respond(response, UNAUTHORIZED, SERVER_AUTHENTICATE_ERROR);
    }
}
