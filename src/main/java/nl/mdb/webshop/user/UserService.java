package nl.mdb.webshop.user;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        String hash = encoder.encode(user.getPassword());
        user.setPassword(hash);
        return userRepository.save(user);
    }
}
