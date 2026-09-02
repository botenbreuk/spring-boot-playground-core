package nl.rdb.springbootplayground.user;

import static org.assertj.core.api.Assertions.assertThat;

import nl.rdb.springbootplayground._testdata.fixtures.UserFixtures;
import nl.rdb.springbootplayground.test.AbstractIntegrationTest;
import nl.rdb.springbootplayground.user.mapper.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserMapperTest extends AbstractIntegrationTest {

    @Autowired
    private UserFixtures userFixtures;

    @Test
    void shouldMapUserToUserResult() {
        User user = userFixtures.admin();
        user.setFirstName(null);

        //when
        UserResult userResult = UserMapper.INSTANCE.userToUserResult(user);

        //then
        assertThat(userResult).isNotNull();
        assertThat(userResult.email).isEqualTo(user.getEmail());
        assertThat(userResult.username).isEqualTo(user.getUsername());
        assertThat(userResult.firstName).isNull();
        assertThat(userResult.lastName).isEqualTo(user.getLastName());
    }
}
