package nl.rdb.springbootplayground.user;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("@userSecurityUtils.create()")
    public User create(User user) {
        return save(user);
    }

    @PreAuthorize("@userSecurityUtils.update()")
    public User update(User user) {
        return save(user);
    }

    private User save(User user) {
        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);
        return userRepository.save(user);
    }

    @PreAuthorize("@userSecurityUtils.delete()")
    public void delete(User user) {
        userRepository.delete(user);
    }

    public void getProj() {
        userRepository.findJeMoederByEmail("piet@test.nl");
        userRepository.findByLastName("van den Lange");
    }

    public void saveMultiple2() {
        User user1 = new User();
        user1.setEmail("user1");
        user1.setPassword("password");
        user1.setPhone("");
        user1.setFirstName("");
        user1.setLastName("");
        user1.setRole(UserRole.ADMIN);
        user1.setActief(true);

        User user2 = new User();
        user2.setEmail("user2");
        user2.setPassword("password");
        user2.setPhone("");
        user2.setFirstName("");
        user2.setLastName("");
        user2.setRole(UserRole.ADMIN);
        user2.setActief(true);

        User user3 = new User();
        user3.setEmail("user3");
        user3.setPassword("password");
        user3.setPhone("");
        user3.setFirstName("");
        user3.setLastName("");
        user3.setRole(UserRole.ADMIN);
        user3.setActief(true);

        User user4 = new User();
        user4.setEmail("user4");
        user4.setPassword("password");
        user4.setPhone("");
        user4.setFirstName("");
        user4.setLastName("");
        user4.setRole(UserRole.ADMIN);
        user4.setActief(true);

        userRepository.saveAll(List.of(user1, user2, user3, user4));
    }
}
