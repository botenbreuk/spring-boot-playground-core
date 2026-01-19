package nl.rdb.springbootplayground._testdata.fixtures;

import static nl.rdb.springbootplayground.user.UserRole.ADMIN;

import lombok.RequiredArgsConstructor;
import nl.rdb.springbootplayground.user.User;
import nl.rdb.springbootplayground.user.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFixtures {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User base() {
        User user = new User();
        user.setEmail("admin@test.nl");
        user.setPassword(passwordEncoder.encode("Welkom42!!"));
        user.setPhone("+31612121212");
        user.setFirstName("Sjonny");
        user.setLastName("Bever");
        user.setActief(true);
        user.setRole(ADMIN);

        return user;
    }

    public User admin() {
        User user = base();
        return userRepository.save(user);
    }

    public User sjonnyb() {
        User user = base();
        user.setEmail("sjonnyb@test.nl");
        return userRepository.save(user);
    }

    public User piet() {
        User user = base();
        user.setEmail("piet@test.nl");
        user.setFirstName("Piet");
        user.setLastName("van den Lange");
        return userRepository.save(user);
    }
}
