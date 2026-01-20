/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker.mail;

import static nl.rdb.springbootplayground.shared.docker.DockerConfig.MAILPIT_HTTP_PORT_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.DockerConfig.SPRING_MAIL_HOST_PROPERTY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.ALL;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class MailpitClient {

    private static final String MESSAGE_PATH = "/api/v1/message/{messageId}";
    private static final String MESSAGES_PATH = "/api/v1/messages";

    private final RestClient restClient;

    public MailpitClient(Environment env) {
        Integer httpPort = env.getProperty(MAILPIT_HTTP_PORT_PROPERTY, Integer.class);
        String host = env.getProperty(SPRING_MAIL_HOST_PROPERTY);
        JacksonJsonHttpMessageConverter jsonMessageConverter = new JacksonJsonHttpMessageConverter();
        jsonMessageConverter.setSupportedMediaTypes(List.of(ALL));
        this.restClient = RestClient.builder()
                .baseUrl("http://%s:%d".formatted(host, httpPort))
                .configureMessageConverters(conf -> conf.addCustomConverter(jsonMessageConverter))
                .build();
    }

    public List<MailpitMessage> getMessages() {
        final MailpitMessages response = restClient.get()
                .uri(MESSAGES_PATH)
                .retrieve()
                .toEntity(MailpitMessages.class)
                .getBody();
        List<MailpitMessage> messages = response.getMessages();
        messages.forEach(m -> log.info(m.getSubject()));
        log.info("Retrieved {} messages from Mailpit", messages.size());
        return messages;
    }

    public MailpitMessageDetails getHtmlMessage(MailpitMessage message) {
        assertNotNull(message);
        return restClient.get()
                .uri(MESSAGE_PATH, message.getId())
                .retrieve()
                .toEntity(MailpitMessageDetails.class)
                .getBody();
    }

    public void deleteAll() {
        restClient.delete()
                .uri(MESSAGES_PATH)
                .retrieve()
                .toBodilessEntity();
    }

    public Set<String> getAllRecipients() {
        return getMessages().stream()
                .flatMap(v -> v.getRecipientsTo().stream())
                .collect(Collectors.toSet());
    }
}