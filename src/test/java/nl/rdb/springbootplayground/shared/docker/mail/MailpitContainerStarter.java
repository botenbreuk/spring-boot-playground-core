package nl.rdb.springbootplayground.shared.docker.mail;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
public class MailpitContainerStarter {

    public static final String SPRING_MAIL_HOST_PROPERTY = "spring.mail.host";
    public static final String SPRING_MAIL_PORT_PROPERTY = "spring.mail.port";
    public static final String MAILPIT_HTTP_PORT_PROPERTY = "mailpit.http-port";

    public static final int MAILPIT_SMTP_PORT = 1025;
    public static final int MAILPIT_HTTP_PORT = 8025;

    private final GenericContainer<?> container;
    private final ConfigurableEnvironment env;

    private MailpitContainerStarter(ConfigurableEnvironment env) {
        this.env = env;
        this.container = new GenericContainer<>("axllent/mailpit")
                .withExposedPorts(MAILPIT_SMTP_PORT, MAILPIT_HTTP_PORT)
                .waitingFor(Wait.forHttp("/").forPort(MAILPIT_HTTP_PORT));
    }

    public static MailpitContainerStarter with(ConfigurableEnvironment env) {
        return new MailpitContainerStarter(env);
    }

    public void start() {
        this.container.start();
        log.info("----------------------------------------------------------------");
        log.info("| Mailpit smtp host override: {} |", overrideSmtpHostAndPort());
        log.info("----------------------------------------------------------------");
    }

    private String overrideSmtpHostAndPort() {
        env.getSystemProperties().put(SPRING_MAIL_HOST_PROPERTY, container.getHost());
        env.getSystemProperties().put(SPRING_MAIL_PORT_PROPERTY, container.getMappedPort(MAILPIT_SMTP_PORT));
        env.getSystemProperties().put(MAILPIT_HTTP_PORT_PROPERTY, container.getMappedPort(MAILPIT_HTTP_PORT));
        return env.getProperty(SPRING_MAIL_HOST_PROPERTY) + ":" + env.getProperty(SPRING_MAIL_PORT_PROPERTY);
    }
}
