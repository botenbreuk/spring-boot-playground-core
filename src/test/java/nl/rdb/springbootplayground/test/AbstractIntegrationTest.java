package nl.rdb.springbootplayground.test;

import nl.rdb.springbootplayground.shared.docker.mail.MailpitClient;
import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
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
}
