package nl.rdb.springbootplayground.shared.security;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.concurrent.Callable;

import nl.rdb.springbootplayground.config.security.AuthenticationAdapter;
import nl.rdb.springbootplayground.config.security.user.UserDetailsAdapter;
import nl.rdb.springbootplayground.shared.security.annotation.SecurityUtil;
import nl.rdb.springbootplayground.user.User;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

@SecurityUtil
public record SecurityUtils() {

    public static <T> T runAsSystem(Callable<T> task) {
        Authentication auth = getContext().getAuthentication();
        try {
            getContext().setAuthentication(new AuthenticationAdapter(User.SYSTEM_USER));
            return task.call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            getContext().setAuthentication(auth);
        }
    }

    public static User getCurrentUser() {
        if (getContext().getAuthentication() == null) {
            throw new AuthenticationCredentialsNotFoundException("Not authenticated.");
        }

        Authentication authentication = getContext().getAuthentication();
        return (User) ((UserDetailsAdapter<?>) authentication.getPrincipal()).user();
    }
}
