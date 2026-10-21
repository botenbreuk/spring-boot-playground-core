package nl.rdb.springbootplayground.user;

import java.util.List;

import io.beanmapper.BeanMapper;
import io.beanmapper.spring.web.MergedForm;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BeanMapper beanMapper;

    @GetMapping
    List<UserResult> getAllUsers(GebruikerFilter filter) {
        return beanMapper.map(userService.findAll(filter), UserResult.class);
    }

    @PostMapping
    UserResult create(@MergedForm(UserForm.class) User user) {
        User created = userService.create(user);
        return beanMapper.map(created, UserResult.class);
    }

    @PutMapping("/{user}")
    UserResult update(@MergedForm(value = UserForm.class, mergeId = "user") User user) {
        User updated = userService.update(user);
        return beanMapper.map(updated, UserResult.class);
    }

    @DeleteMapping("/{user}")
    void delete(@PathVariable User user) {
        userService.delete(user);
    }
}
