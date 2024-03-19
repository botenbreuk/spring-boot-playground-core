/*
 * Copyright (c) 2020. 42 bv (www.42.nl). All rights reserved.
 */

package nl.mdb.webshop.shared.docker;

import lombok.extern.slf4j.Slf4j;
import nl.mdb.webshop.shared.docker.mail.MailHogContainerStarter;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * This {@link ApplicationListener} is registered in `src/test/resources/config/application.yml`
 * by setting the `context.listener.classes` property. This listener triggers on {@link ApplicationEnvironmentPreparedEvent}
 * to make sure only the environment properties are loaded and no other (auto-)configuration
 * is started yet.
 * <p>
 * It checks the `docker.postgres.enabled` property and starts a postgres docker container using the testcontainers.org library
 * when value is true.
 * The public jdbc url (with randomly generated public port) is set to override the `spring.datasource.url`
 * property which is used by the {@link javax.sql.DataSource} bean configured in
 * {@link org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration}
 */
@Slf4j
public class TestApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    public static final String DOCKER_POSTGRES_ENABLED = "docker.postgres.enabled";
    public static final String DOCKER_MAILHOG_ENABLED = "docker.mailhog.enabled";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();

        log.info("<<<<< SHOULD START POSTGRES CONTAINER: {} >>>>>", isDockerPostgresEnabled(env));
        log.info("<<<<< SHOULD START MAILHOG CONTAINER: {} >>>>>", isDockerMailhogEnabled(env));

        if (isDockerPostgresEnabled(env)) {
            PostgresContrainerStarter.with(env).start();
        }
        if (isDockerMailhogEnabled(env)) {
            MailHogContainerStarter.with(env).start();
        }
    }

    private boolean isDockerPostgresEnabled(Environment env) {
        return env.getProperty(DOCKER_POSTGRES_ENABLED, Boolean.class, false);
    }

    private boolean isDockerMailhogEnabled(Environment env) {
        return env.getProperty(DOCKER_MAILHOG_ENABLED, Boolean.class, false);
    }
}
