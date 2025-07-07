/*
 * Copyright (c) 2020. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker.postgres;

import static org.testcontainers.containers.PostgreSQLContainer.DEFAULT_TAG;
import static org.testcontainers.containers.PostgreSQLContainer.IMAGE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgresContainerStarter {

    public static final String DOCKER_POSTGRES_VERSION_PROPERTY = "docker.postgres.image-version";
    public static final String JDBC_USERNAME_PROPERTY = "spring.datasource.username";
    public static final String JDBC_PASSWORD_PROPERTY = "spring.datasource.password";
    public static final String JDBC_URL_PROPERTY = "spring.datasource.url";
    public static final String DOCKER_POSTGRES_STATIC_PORT_PROPERTY = "docker.postgres.static-port";

    private final PostgreSQLContainer container;
    private final ConfigurableEnvironment env;

    public static PostgresContainerStarter with(ConfigurableEnvironment env) {
        return new PostgresContainerStarter(env);
    }

    private PostgresContainerStarter(ConfigurableEnvironment env) {
        this.env = env;
        this.container = new PostgreSQLContainer<>(getPostgresImage())
                .withUsername(env.getRequiredProperty(JDBC_USERNAME_PROPERTY))
                .withPassword(env.getRequiredProperty(JDBC_PASSWORD_PROPERTY));

        if (env.getProperty(DOCKER_POSTGRES_STATIC_PORT_PROPERTY, boolean.class, false)) {
            try {
                String jdbcUrl = env.getProperty(JDBC_URL_PROPERTY, "jdbc:");
                URI jdbcUri = new URI(jdbcUrl.substring(5));

                // Unfortunately, the method addFixedExposedPort(), which is needed to assign a static port, is protected and cannot be accessed regularly.
                Method addFixedExposedPort = GenericContainer.class.getDeclaredMethod("addFixedExposedPort", int.class, int.class);
                addFixedExposedPort.setAccessible(true);
                addFixedExposedPort.invoke(this.container, jdbcUri.getPort(),
                        5432); // Container port is always 5432. Host port is what we've specified in the application.yml
            } catch (URISyntaxException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Error assigning static Postgres port", e);
            }
        }
    }

    public PostgreSQLContainer<?> start() {
        this.container.start();
        log.info("----------------------------------------------------------------");
        log.info("| Postgres jdbc url override: {} |", overrideJdbcUrl());
        log.info("----------------------------------------------------------------");
        return this.container;
    }

    private String getPostgresImage() {
        return IMAGE + ":" + env.getProperty(DOCKER_POSTGRES_VERSION_PROPERTY, DEFAULT_TAG);
    }

    private String overrideJdbcUrl() {
        env.getSystemProperties().put(JDBC_URL_PROPERTY, container.getJdbcUrl());
        return env.getProperty(JDBC_URL_PROPERTY);
    }
}
