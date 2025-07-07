package nl.rdb.springbootplayground.shared.docker.mail;

import static nl.rdb.springbootplayground.shared.docker.mail.MailpitContainerStarter.MAILPIT_HTTP_PORT_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.mail.MailpitContainerStarter.SPRING_MAIL_HOST_PROPERTY;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MailpitClient {

    public static final String GET_MESSAGES = "/api/v1/messages";
    public static final String VIEW_HTML_MESSAGE = "/api/v1/message/%s";
    public static final String DELETE_MESSAGES = "/api/v1/messages";

    private final RestTemplate restTemplate;

    public MailpitClient(Environment env) {
        int httpPort = env.getProperty(MAILPIT_HTTP_PORT_PROPERTY, Integer.class);
        String host = env.getProperty(SPRING_MAIL_HOST_PROPERTY);
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setSupportedMediaTypes(List.of(MediaType.ALL));
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(String.format("http://%s:%s", host, httpPort))
                .additionalMessageConverters(jsonMessageConverter)
                .build();
    }

    public List<MailpitMessage> getMessages() {
        final ResponseEntity<MailpitMessages> response = restTemplate.getForEntity(
                createUrl(GET_MESSAGES),
                MailpitMessages.class);
        List<MailpitMessage> messages = response.getBody().messages;
        messages.forEach(m -> log.info(m.getSubject()));
        log.info("Retrieved {} messages from Mailpit", messages.size());
        return messages;
    }

    public MailpitMessageDetails getHtmlMessage(MailpitMessage message) {
        assertNotNull(message);
        return restTemplate.getForEntity(
                        createUrl(VIEW_HTML_MESSAGE.formatted(message.ID)),
                        MailpitMessageDetails.class)
                .getBody();
    }

    public Set<String> getAllRecipients() {
        return getMessages().stream()
                .flatMap(v -> v.getRecipientsTo().stream())
                .collect(Collectors.toSet());
    }

    public void deleteAll() {
        restTemplate.delete(createUrl(DELETE_MESSAGES));
    }

    private String createUrl(String path, String... values) {
        return String.format(path, values);
    }
}