package nl.rdb.springbootplayground.config.security;

import static nl.rdb.springbootplayground.architecture.ViolationType.PUBLIC_PRE_POST_AUTHORIZE;
import static nl.rdb.springbootplayground.architecture.ViolationType.SERVICE_TRANSACTIONAL;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.architecture.ArchIgnore;
import nl.rdb.springbootplayground.config.security.user.UserDetailsAdapter;
import nl.rdb.springbootplayground.user.User;
import nl.rdb.springbootplayground.user.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ArchIgnore(violation = SERVICE_TRANSACTIONAL)
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @ArchIgnore(violation = PUBLIC_PRE_POST_AUTHORIZE)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailOrUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username: '%s' not found.".formatted(username));
        }

        return new UserDetailsAdapter<>(user.get());
    }
}
