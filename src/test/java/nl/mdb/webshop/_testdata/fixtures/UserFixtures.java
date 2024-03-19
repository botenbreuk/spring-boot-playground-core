package nl.mdb.webshop._testdata.fixtures;

import static nl.mdb.webshop.user.UserRole.ADMIN;

import lombok.RequiredArgsConstructor;
import nl.mdb.webshop.user.User;
import nl.mdb.webshop.user.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFixtures {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User base() {
        User user = new User();
        user.setEmail("mike@webshop.nl");
        user.setPassword(passwordEncoder.encode("Welkom42!!"));
        user.setPhone("+31612121212");
        user.setFirstName("Mike");
        user.setLastName("de Boer");
        user.setActief(true);
        user.setRole(ADMIN);

        return user;
    }

    public User mike() {
        User user = base();
        return userRepository.save(user);
    }

    public User piet() {
        User user = base();
        user.setEmail("piet@webshop.nl");
        user.setFirstName("Piet");
        user.setLastName("van den Lange");
        return userRepository.save(user);
    }
}
