package nl.rdb.springbootplayground.authentication;

import static java.util.function.Predicate.not;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.Optional;

import io.beanmapper.BeanMapper;
import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.config.security.user.UserDetailsAdapter;
import nl.rdb.springbootplayground.user.User;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final BeanMapper beanMapper;

    @PostMapping
    public AuthResult authenticate() {
        return current();
    }

    @GetMapping("/current")
    public AuthResult current() {
        return beanMapper.map(getCurrent(), AuthResult.class);
    }

    private User getCurrent() {
        return Optional.ofNullable(getContext().getAuthentication())
                .filter(not(AnonymousAuthenticationToken.class::isInstance))
                .map(auth -> {
                    if (auth.getPrincipal() instanceof UserDetailsAdapter<?>(User user)) {
                        return user;
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}
