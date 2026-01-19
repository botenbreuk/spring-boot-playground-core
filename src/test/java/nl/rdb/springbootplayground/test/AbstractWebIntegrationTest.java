package nl.rdb.springbootplayground.test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import nl.rdb.springbootplayground._testdata.fixtures.UserFixtures;
import nl.rdb.springbootplayground.config.security.user.UserDetailsAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractWebIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected UserFixtures userFixtures;

    protected RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context)
                .configureServer(defaultMockMvcBuilder -> defaultMockMvcBuilder
                        .alwaysDo(log())
                        .apply(springSecurity())
                        .defaultRequest(get("/")
                                .contentType(APPLICATION_JSON)
                                .with(csrf())
                                .with(user(new UserDetailsAdapter<>(userFixtures.admin())))
                        )
                )
                .build();
    }
}
