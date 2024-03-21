package nl.mdb.webshop.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    UserResult create(@RequestBody UserForm form) {
        return Optional.of(userService.create(User.toCreateForm(form)))
                .map(UserResult::new)
                .orElse(null);
    }

    @PutMapping("/{userId}")
    UserResult update(@PathVariable Long userId, @RequestBody UserForm form) {
        return Optional.of(userService.create(User.toUpdateForm(userId, form)))
                .map(UserResult::new)
                .orElse(null);
    }

    @DeleteMapping
    void delete(@PathVariable User user) {
        userService.delete(user);
    }
}
