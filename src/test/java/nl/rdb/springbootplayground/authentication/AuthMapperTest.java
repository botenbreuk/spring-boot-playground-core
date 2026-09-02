package nl.rdb.springbootplayground.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import nl.rdb.springbootplayground.authentication.mapper.AuthMapper;
import nl.rdb.springbootplayground.user.User;
import nl.rdb.springbootplayground.user.UserRole;

import org.junit.jupiter.api.Test;

class AuthMapperTest {

    @Test
    void shouldMapUserToAuthenticatedAuthResult() {
        User user = new User();
        user.setEmail("admin@test.nl");
        user.setUsername("admin");
        user.setFirstName("Admin");
        user.setLastName(null);
        user.setRole(UserRole.ADMIN);

        AuthResult authResult = AuthMapper.INSTANCE.userToAuthResult(user);

        assertThat(authResult.user()).isNotNull();
        assertThat(authResult.user().authenticated).isTrue();
        assertThat(authResult.user().email).isEqualTo(user.getEmail());
        assertThat(authResult.user().username).isEqualTo(user.getUsername());
        assertThat(authResult.user().firstName).isEqualTo(user.getFirstName());
        assertThat(authResult.user().lastName).isNull();
        assertThat(authResult.user().role).isEqualTo(user.getRole());
    }

    @Test
    void shouldMapNullUserToUnauthenticatedAuthResult() {
        AuthResult authResult = AuthMapper.INSTANCE.userToAuthResult(null);

        assertThat(authResult.user()).isNotNull();
        assertThat(authResult.user().authenticated).isFalse();
        assertThat(authResult.user().email).isNull();
    }
}
