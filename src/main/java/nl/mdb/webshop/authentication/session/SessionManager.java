package nl.mdb.webshop.authentication.session;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import nl.mdb.webshop.config.security.user.UserDetailsAdapter;
import nl.mdb.webshop.user.User;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionManager {

    private final SessionRegistry sessionRegistry;

    public void invalidate(User value) {
        sessionRegistry.getAllPrincipals().stream().map(v -> {
                    if (v instanceof UserDetailsAdapter<?> user) {
                        return user;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(u -> u.user() instanceof User)
                .filter(u -> ((User) u.user()).getId().equals(value.getId()))
                .forEach(v -> sessionRegistry.getAllSessions(v, false).forEach(SessionInformation::expireNow));
    }
}
