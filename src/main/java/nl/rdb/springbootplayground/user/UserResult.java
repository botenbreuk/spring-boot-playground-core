package nl.rdb.springbootplayground.user;

import lombok.Getter;

@Getter
public class UserResult {

    private final Long id;
    private final String email;
    private final String phone;
    private final String firstName;
    private final String lastName;
    private final boolean authenticated;

    public UserResult(User user) {
        if (user != null) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.authenticated = true;
        } else {
            this.id = null;
            this.email = null;
            this.phone = null;
            this.firstName = null;
            this.lastName = null;
            this.authenticated = false;
        }
    }
}
