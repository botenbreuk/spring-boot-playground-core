package nl.rdb.springbootplayground.test;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import nl.rdb.springbootplayground.config.security.AuthenticationAdapter;
import nl.rdb.springbootplayground.shared.docker.mail.MailpitClient;
import nl.rdb.springbootplayground.user.User;
import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "unit-test" })
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MailpitClient mailpitClient;

    @Autowired
    protected JsonMapper jsonMapper;

    @BeforeEach
    void clearMailHogInbox() {
        mailpitClient.deleteAll();
    }

    protected void initSystemAuth() {
        getContext().setAuthentication(new AuthenticationAdapter(User.SYSTEM_USER));
    }
}
