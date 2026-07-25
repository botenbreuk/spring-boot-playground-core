package nl.rdb.springbootplayground.email;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.config.mail")
public class MailConfigProperties {

    private String subjectPrefix;
    private String from;
    private String replyTo;
    private boolean onlyLogDontSend = false;
}