/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.mdb.webshop.shared.docker.mail;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
public class MailHogContainerStarter {

    public static final String SPRING_MAIL_HOST_PROPERTY = "spring.mail.host";
    public static final String SPRING_MAIL_PORT_PROPERTY = "spring.mail.port";
    public static final String MAILHOG_HTTP_PORT_PROPERTY = "mailhog.http-port";

    public static final int MAILHOG_SMTP_PORT = 1025;
    public static final int MAILHOG_HTTP_PORT = 8025;

    private final GenericContainer<?> container;
    private final ConfigurableEnvironment env;

    private MailHogContainerStarter(ConfigurableEnvironment env) {
        this.env = env;
        this.container = new GenericContainer<>("mailhog/mailhog")
                .withExposedPorts(MAILHOG_SMTP_PORT, MAILHOG_HTTP_PORT)
                .waitingFor(Wait.forHttp("/").forPort(MAILHOG_HTTP_PORT));
    }

    public static MailHogContainerStarter with(ConfigurableEnvironment env) {
        return new MailHogContainerStarter(env);
    }

    public GenericContainer start() {
        this.container.start();
        log.info("----------------------------------------------------------------");
        log.info("| Mailhog smtp host override: {} |", overrideSmtpHostAndPort());
        log.info("----------------------------------------------------------------");
        return this.container;
    }

    private String overrideSmtpHostAndPort() {
        env.getSystemProperties().put(SPRING_MAIL_HOST_PROPERTY, container.getContainerIpAddress());
        env.getSystemProperties().put(SPRING_MAIL_PORT_PROPERTY, container.getMappedPort(MAILHOG_SMTP_PORT));
        env.getSystemProperties().put(MAILHOG_HTTP_PORT_PROPERTY, container.getMappedPort(MAILHOG_HTTP_PORT));
        return env.getProperty(SPRING_MAIL_HOST_PROPERTY) + ":" + env.getProperty(SPRING_MAIL_PORT_PROPERTY);
    }
}
