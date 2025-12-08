package nl.rdb.springbootplayground.config.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rdb.springbootplayground.config.error.GenericErrorHandler;
import tools.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationFilter extends OncePerRequestFilter {

    private static final String SERVER_LOGIN_FAILED_ERROR = "Incorrect username or password.";
    private static final String ACCOUNT_TEMPORARILY_LOCKED = "Your account has been temporarily locked.";
    private static final String ACCOUNT_DISABLED = "Your account has been administratively disabled.";

    private final GenericErrorHandler errorHandler;
    private final RequestMatcher matcher;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (matcher.matches(request)) {
            LoginForm form = objectMapper.readValue(request.getInputStream(), LoginForm.class);
            log.info("Attempting to login for {}", form.username);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(form.username, form.password);
            try {
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (AuthenticationException ex) {
                handleLoginFailure(response, form.username, ex);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void handleLoginFailure(HttpServletResponse response, String username, AuthenticationException ae) throws IOException {
        if (ae instanceof AccountStatusException) {
            if (ae instanceof LockedException) {
                errorHandler.respond(response, UNAUTHORIZED, ACCOUNT_TEMPORARILY_LOCKED);
            } else {
                errorHandler.respond(response, UNAUTHORIZED, ACCOUNT_DISABLED);
            }
        } else {
            errorHandler.respond(response, UNAUTHORIZED, SERVER_LOGIN_FAILED_ERROR);
        }
        log.warn("Login Failed for {} : {}", username, ae.getMessage());
    }
}
