package nl.rdb.springbootplayground.test;

import nl.rdb.springbootplayground.shared.docker.mail.MailHogClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "unit-test" })
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MailHogClient mailHogClient;

    @BeforeEach
    void clearMailHogInbox() {
        mailHogClient.deleteAll();
    }
}
