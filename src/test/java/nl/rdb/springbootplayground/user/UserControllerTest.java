package nl.rdb.springbootplayground.user;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.rdb.springbootplayground._testdata.fixtures.UserFixtures;
import nl.rdb.springbootplayground.test.AbstractWebIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserControllerTest extends AbstractWebIntegrationTest {

    @Autowired
    private UserFixtures userFixtures;

    @Test
    void findAllGebruikers() throws Exception {
        userFixtures.sjonnyb();

        webClient.perform(get("/gebruikers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
