package nl.rdb.springbootplayground.shared.docker.postgres;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgresContainerStarterAlt extends PostgreSQLContainer<PostgresContainerStarterAlt> {

    public static final String JDBC_URL_PROPERTY = "spring.datasource.url";

    private static final String DOCKER_POSTGRES_VERSION_PROPERTY = "docker.postgres.image-version";
    private static final String JDBC_USERNAME_PROPERTY = "spring.datasource.username";
    private static final String JDBC_PASSWORD_PROPERTY = "spring.datasource.password";
    private static final String DOCKER_POSTGRES_STATIC_PORT_PROPERTY = "docker.postgres.static-port";

    public PostgresContainerStarterAlt(ConfigurableEnvironment env) {
        super("%s:%s".formatted(IMAGE, env.getProperty(DOCKER_POSTGRES_VERSION_PROPERTY, DEFAULT_TAG)));
        this.withUsername(env.getRequiredProperty(JDBC_USERNAME_PROPERTY));
        this.withPassword(env.getRequiredProperty(JDBC_PASSWORD_PROPERTY));

        if (env.getProperty(DOCKER_POSTGRES_STATIC_PORT_PROPERTY, boolean.class, false)) {
            var jdbcUri = URI.create(env.getProperty(JDBC_URL_PROPERTY, "jdbc:").substring(5));
            this.addFixedExposedPort(jdbcUri.getPort(), POSTGRESQL_PORT);
        }
    }

    @Override
    public void start() {
        super.start();
        log.info("----------------------------------------------------------------");
        log.info("| Postgres jdbc url override: {} |", this.getJdbcUrl());
        log.info("----------------------------------------------------------------");
    }
}
