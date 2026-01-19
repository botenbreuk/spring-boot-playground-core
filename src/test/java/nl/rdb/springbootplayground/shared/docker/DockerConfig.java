package nl.rdb.springbootplayground.shared.docker;

import static com.github.dockerjava.api.model.Ports.Binding.bindPort;
import static org.testcontainers.postgresql.PostgreSQLContainer.IMAGE;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;

@Slf4j
@Configuration
public class DockerConfig {

    public static final String DOCKER_POSTGRES_ENABLED = "docker.postgres.enabled";
    public static final String DOCKER_MAILPIT_ENABLED = "docker.mailpit.enabled";

    public static final int DOCKER_POSTGRES_STATIC_PORT = 5432;
    public static final int MAILPIT_SMTP_PORT = 1025;
    public static final int MAILPIT_WEB_PORT = 8025;

    @Value("${docker.postgres.image-version}")
    private String dockerPostgresImageVersion;
    @Value("${docker.postgres.static-port}")
    private boolean postgresStaticPortEnabled;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @ServiceConnection
    @ConditionalOnBooleanProperty(DOCKER_POSTGRES_ENABLED)
    PostgreSQLContainer postgresContainer() {
        String postgresImage = "%s:%s".formatted(IMAGE, dockerPostgresImageVersion);
        PostgreSQLContainer container = new PostgreSQLContainer(postgresImage)
                .withUsername(username)
                .withPassword(password)
                .withEnv("TZ", "Europe/Amsterdam")
                .withEnv("PGTZ", "Europe/Amsterdam");

        if (postgresStaticPortEnabled) {
            container.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(
                                    bindPort(DOCKER_POSTGRES_STATIC_PORT),
                                    new ExposedPort(DOCKER_POSTGRES_STATIC_PORT))
                    )
            ));
        }

        container.start();
        log.info("----------------------------------------------------------------");
        log.info("| Postgres jdbc url override: {} |", container.getJdbcUrl());
        log.info("| Postgres username override: {} |", container.getUsername());
        log.info("| Postgres password override: {} |", container.getPassword());
        log.info("----------------------------------------------------------------");
        return container;
    }

    @Bean
    @ConditionalOnBooleanProperty(DOCKER_MAILPIT_ENABLED)
    GenericContainer<?> mailpitContainer() {
        GenericContainer<?> container = new GenericContainer<>("axllent/mailpit:latest")
                .withExposedPorts(MAILPIT_SMTP_PORT, MAILPIT_WEB_PORT)
                .waitingFor(Wait.forLogMessage(".*accessible via.*", 1));
        container.start();
        log.info("-----------------------------------------------------------------");
        log.info("| Mailpit smtp host override: {}:{} |",
                container.getMappedPort(MAILPIT_SMTP_PORT),
                container.getMappedPort(MAILPIT_WEB_PORT));
        log.info("-----------------------------------------------------------------");
        return container;
    }

    @Bean
    @ConditionalOnBooleanProperty(DOCKER_MAILPIT_ENABLED)
    DynamicPropertyRegistrar mailpitProperties(GenericContainer<?> mailpitContainer) {
        return registry -> {
            registry.add("spring.mail.host", mailpitContainer::getHost);
            registry.add("spring.mail.port", () -> mailpitContainer.getMappedPort(MAILPIT_SMTP_PORT));
            registry.add("mailpit.web.port", () -> mailpitContainer.getMappedPort(MAILPIT_WEB_PORT));
        };
    }
}
