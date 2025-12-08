package nl.rdb.springbootplayground.user;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.rdb.springbootplayground._testdata.fixtures.UserFixtures;
import nl.rdb.springbootplayground.test.AbstractWebIntegrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserControllerTest extends AbstractWebIntegrationTest {

    @Autowired
    private UserFixtures userFixtures;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
    }

    @Nested
    class FindAll {

        @Test
        void findAllGebruikers() throws Exception {
            userFixtures.sjonnyb();

            webClient.perform(get("/gebruikers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        void findAllGebruikers_filterByEmail() throws Exception {
            User piet = userFixtures.piet();
            userFixtures.sjonnyb();

            webClient.perform(get("/gebruikers")
                            .param("email", "piet")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].email").value(piet.getEmail()));
        }
    }
}
