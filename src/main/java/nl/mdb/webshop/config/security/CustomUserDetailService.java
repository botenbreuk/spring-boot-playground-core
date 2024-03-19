package nl.mdb.webshop.config.security;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nl.mdb.webshop.config.security.user.UserDetailsAdapter;
import nl.mdb.webshop.user.User;
import nl.mdb.webshop.user.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username: '%s' not found.".formatted(username));
        }

        return new UserDetailsAdapter<>(user.get());
    }
}
