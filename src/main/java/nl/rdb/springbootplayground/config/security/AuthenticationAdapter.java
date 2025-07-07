package nl.rdb.springbootplayground.config.security;

import nl.rdb.springbootplayground.config.security.user.RegisteredUser;
import nl.rdb.springbootplayground.config.security.user.UserDetailsAdapter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationAdapter extends UsernamePasswordAuthenticationToken {

    public AuthenticationAdapter(RegisteredUser user) {
        this(new UserDetailsAdapter<>(user));
    }

    private AuthenticationAdapter(UserDetailsAdapter<?> details) {
        super(details, "", details.getAuthorities());
    }
}