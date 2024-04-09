package nl.rdb.springbootplayground.config.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginForm {

    public String username;
    public String password;

    public LoginForm(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
