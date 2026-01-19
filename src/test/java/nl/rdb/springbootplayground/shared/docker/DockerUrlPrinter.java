package nl.rdb.springbootplayground.shared.docker;

import static nl.rdb.springbootplayground.shared.docker.DockerConfig.DOCKER_MAILPIT_ENABLED;
import static nl.rdb.springbootplayground.shared.docker.DockerConfig.DOCKER_POSTGRES_ENABLED;
import static nl.rdb.springbootplayground.shared.docker.DockerConfig.MAILPIT_WEB_PORT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBooleanProperty(DOCKER_POSTGRES_ENABLED)
@ConditionalOnBooleanProperty(DOCKER_MAILPIT_ENABLED)
public class DockerUrlPrinter {

    private final PostgreSQLContainer postgresContainer;
    private final GenericContainer<?> mailpitContainer;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("------------------- Docker application database: -----------------------");
        log.info("| jdbc url: {}", postgresContainer.getJdbcUrl());
        log.info("| username: {}", postgresContainer.getUsername());
        log.info("| password: {}", postgresContainer.getPassword());
        log.info("------------------- Docker MailPit: -----------------------------");
        log.info("| http://{}:{}",
                mailpitContainer.getHost(),
                mailpitContainer.getMappedPort(MAILPIT_WEB_PORT));
        log.info("-----------------------------------------------------------------");
    }
}
