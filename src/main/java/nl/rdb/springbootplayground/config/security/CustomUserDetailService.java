package nl.rdb.springbootplayground.config.security;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.user.UserRepository;

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
        //        Optional<User> user = userRepository.findByEmail(username);
        //
        //        if (user.isEmpty()) {
        //            throw new UsernameNotFoundException("Username: '%s' not found.".formatted(username));
        //        }
        //
        //        return new UserDetailsAdapter<>(user.get());
        return null;
    }
}
