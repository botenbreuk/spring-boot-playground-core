/*
 * Copyright (c) 2020. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker;

import static nl.rdb.springbootplayground.shared.docker.PostgresContrainerStarter.JDBC_URL_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.TestApplicationListener.DOCKER_MAILHOG_ENABLED;
import static nl.rdb.springbootplayground.shared.docker.mail.MailHogContainerStarter.MAILHOG_HTTP_PORT_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.mail.MailHogContainerStarter.SPRING_MAIL_HOST_PROPERTY;

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
        if (env.getProperty(DOCKER_MAILHOG_ENABLED, Boolean.class, false)) {
            log.info("----------------- MailHog http hostAndPort ------------------");
            log.info("| http://{}:{}                                    |",
                    env.getProperty(SPRING_MAIL_HOST_PROPERTY),
                    env.getProperty(MAILHOG_HTTP_PORT_PROPERTY));
            log.info("-------------------------------------------------------------");
        }
    }
}
