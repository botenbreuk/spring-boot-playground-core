package nl.mdb.webshop.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Use webClient directly to issue a HttpRequest as ADMIN user with a valid csrf token.
 * Use mockMvcBuilder to configure a custom default request.
 *
 * @author bas
 */
public abstract class AbstractWebIntegrationTest extends AbstractIntegrationTest {

    protected MockMvc webClient;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void initWebClient() {
        webClient = webAppContextSetup(webApplicationContext)
                .alwaysDo(log())
                .build();
    }
}
