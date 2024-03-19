package nl.mdb.webshop.authentication;

import static java.util.function.Predicate.not;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.Optional;

import nl.mdb.webshop.config.security.user.UserDetailsAdapter;
import nl.mdb.webshop.user.User;
import nl.mdb.webshop.user.UserResult;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @PostMapping
    public AuthResult authenticate() {
        return current();
    }

    @GetMapping("/current")
    public AuthResult current() {
        return new AuthResult(new UserResult(getCurrent()));
    }

    private User getCurrent() {
        return Optional.ofNullable(getContext().getAuthentication())
                .filter(not(AnonymousAuthenticationToken.class::isInstance))
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetailsAdapter<?> adapter) {
                        return (User) adapter.user();
                    } else {
                        return null;
                    }
                }).orElse(null);
    }
}
