package nl.mdb.webshop.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gebruikers")
public class UserController {

    private final UserService userService;

    @GetMapping
    List<UserResult> getAllUsers() {
        return userService.findAll().stream()
                .map(UserResult::new)
                .toList();
    }

    @PostMapping
    UserResult createUser(@RequestBody UserForm form) {
        return Optional.of(userService.save(User.formToUser(form)))
                .map(UserResult::new)
                .orElse(null);
    }
}
