package nl.mdb.webshop.user;

import lombok.Getter;

@Getter
public class UserResult {

    private final Long id;
    private final String email;
    private final String phone;
    private final String firstName;
    private final String lastName;

    public UserResult(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
}
