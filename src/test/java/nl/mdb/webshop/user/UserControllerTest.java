package nl.mdb.webshop.user;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.mdb.webshop._testdata.fixtures.UserFixtures;
import nl.mdb.webshop.test.AbstractWebIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserControllerTest extends AbstractWebIntegrationTest {

    @Autowired
    private UserFixtures userFixtures;

    @Test
    void findAllGebruikers() throws Exception {
        userFixtures.mike();

        webClient.perform(get("/gebruikers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
