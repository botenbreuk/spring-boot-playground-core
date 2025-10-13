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

    public List<User> findAll(GebruikerFilter filter) {
        return userRepository.findAllUsers(filter);
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
}
