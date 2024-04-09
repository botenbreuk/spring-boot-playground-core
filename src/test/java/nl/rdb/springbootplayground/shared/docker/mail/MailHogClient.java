/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker.mail;

import static nl.rdb.springbootplayground.shared.docker.mail.MailHogContainerStarter.MAILHOG_HTTP_PORT_PROPERTY;
import static nl.rdb.springbootplayground.shared.docker.mail.MailHogContainerStarter.SPRING_MAIL_HOST_PROPERTY;

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
public class MailHogClient {

    public static final String GET_MESSAGES = "/api/v2/messages";
    public static final String DELETE_MESSAGES = "/api/v1/messages";

    private final RestTemplate restTemplate;

    public MailHogClient(Environment env) {
        int httpPort = env.getProperty(MAILHOG_HTTP_PORT_PROPERTY, Integer.class);
        String host = env.getProperty(SPRING_MAIL_HOST_PROPERTY);
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setSupportedMediaTypes(List.of(MediaType.ALL));
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(String.format("http://%s:%s", host, httpPort))
                .additionalMessageConverters(jsonMessageConverter)
                .build();
    }

    public List<MailHogMessage> getMessages() {
        final ResponseEntity<MailHogMessages> response = restTemplate.getForEntity(
                createUrl(GET_MESSAGES),
                MailHogMessages.class);
        List<MailHogMessage> messages = response.getBody().items;
        messages.forEach(m -> log.info(m.Content.Headers.Subject.get(0)));
        log.info("Retrieved " + messages.size() + " messages from Mailhog");
        return messages;
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
