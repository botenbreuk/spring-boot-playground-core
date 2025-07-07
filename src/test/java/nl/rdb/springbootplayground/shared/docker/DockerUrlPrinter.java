package nl.rdb.springbootplayground.shared.docker;

import static nl.rdb.springbootplayground.shared.docker.TestApplicationListener.DOCKER_MAILPIT_ENABLED;
import static nl.rdb.springbootplayground.shared.docker.mail.MailpitContainerStarter.MAILPIT_HTTP_PORT_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.mail.MailpitContainerStarter.SPRING_MAIL_HOST_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.postgres.PostgresContainerStarter.JDBC_URL_PROPERTY;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DockerUrlPrinter {

    private final ConfigurableEnvironment env;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("----------------- Application database: ---------------------");
        log.info("| {}    |", env.getProperty(JDBC_URL_PROPERTY));
        if (env.getProperty(DOCKER_MAILPIT_ENABLED, Boolean.class, false)) {
            log.info("----------------- Mailpit http hostAndPort ------------------");
            log.info("| http://{}:{}                                    |",
                    env.getProperty(SPRING_MAIL_HOST_PROPERTY),
                    env.getProperty(MAILPIT_HTTP_PORT_PROPERTY));
            log.info("-------------------------------------------------------------");
        }
    }
}
