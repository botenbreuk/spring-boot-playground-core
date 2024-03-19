package nl.mdb.webshop.config.security.utils;

import static java.util.function.Predicate.not;
import static nl.mdb.webshop.user.UserRole.ADMIN;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.Optional;

import nl.mdb.webshop.config.security.user.UserDetailsAdapter;
import nl.mdb.webshop.user.User;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public class SecurityUtils {

    private SecurityUtils() {}

    public static User getCurrentUser() {
        return Optional.ofNullable(getContext().getAuthentication())
                .filter(not(AnonymousAuthenticationToken.class::isInstance))
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetailsAdapter<?> adapter) {
                        return (User) adapter.user();
                    } else {
                        return null;
                    }
                })
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Not authenticated."));
    }

    public static boolean isAdmin() {
        return getCurrentUser().getRole() == ADMIN;
    }
}