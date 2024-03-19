package nl.mdb.webshop.user;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nl.mdb.webshop.config.security.user.RegisteredUser;
import nl.mdb.webshop.shared.AbstractEntity;

import com.google.common.collect.Sets;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User extends AbstractEntity implements RegisteredUser {

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
        return Sets.newHashSet(this.role.toAuthority());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.actief;
    }

    public static User formToUser(UserForm form) {
        User user = new User();
        user.setEmail(form.email);
        user.setPassword(form.password);
        user.setPhone(form.phone);
        user.setFirstName(form.firstName);
        user.setLastName(form.lastName);

        return user;
    }
}
