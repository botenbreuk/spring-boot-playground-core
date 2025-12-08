package nl.rdb.springbootplayground.user;

import static nl.rdb.springbootplayground.user.UserRole.SYSTEM;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import nl.rdb.springbootplayground.config.security.user.RegisteredUser;
import nl.rdb.springbootplayground.shared.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User extends AbstractEntity implements RegisteredUser {

    @Transient
    public static final User SYSTEM_USER = new User();

    static {
        SYSTEM_USER.setEmail("no-reply@unknown.nl");
        SYSTEM_USER.setRole(SYSTEM);
    }

    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean actief;

    @Override
    public Set<String> getAuthorities() {
        return Set.of(this.role.toAuthority());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.actief;
    }

    public static User toCreateForm(UserForm form) {
        return toUpdateForm(null, form);
    }

    public static User toUpdateForm(Long id, UserForm form) {
        User user = new User();
        user.setId(id);
        user.setEmail(form.email);
        user.setPassword(form.password);
        user.setPhone(form.phone);
        user.setFirstName(form.firstName);
        user.setLastName(form.lastName);

        return user;
    }
}
