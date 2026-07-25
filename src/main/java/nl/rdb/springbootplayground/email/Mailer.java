package nl.rdb.springbootplayground.email;

import static jakarta.mail.Message.RecipientType.CC;
import static jakarta.mail.Message.RecipientType.TO;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Mailer {

    private final JavaMailSender mailSender;
    private final MailConfigProperties config;
    private final TemplateRenderer templateRenderer;

    @Async
    public void sendToEmail(String email, String subject, String template, Map<String, Object> parameters) {
        send(email, null, subject, template, parameters);
    }

    @Async
    public void sendToEmailAndCC(String email, String cc, String subject, String template, Map<String, Object> parameters) {
        send(email, cc, subject, template, parameters);
    }

    @Async
    public void sendToEmailList(List<String> emailList, String subject, String template, Map<String, Object> parameters) {
        final String emails = StringUtils.join(emailList, ',');
        send(emails, null, subject, template, parameters);
    }

    @Async
    public void sendToEmailListAndCCList(List<String> emailList, List<String> ccList, String subject, String template, Map<String, Object> parameters) {
        final String emails = StringUtils.join(emailList, ',');
        final String ccs = StringUtils.join(ccList, ',');
        send(emails, ccs, subject, template, parameters);
    }

    private void send(String emails, String ccs, String subject, String template, Map<String, Object> parameters) {
        if (isEmpty(emails) && isEmpty(ccs)) {
            log.warn("No 'recipients found for email subject '{}', No email send!", subject);
            return;
        }

        try {
            MimeMessage mail = buildMail(emails, ccs, subject, template, parameters);

            if (config.isOnlyLogDontSend()) {
                log.info("Logging mail message content with subject '{}' to: {}, cc: {}", subject, emails, ccs);
                log.info("{}", mail.getContent());
            } else {
                log.debug("Sending mail message with subject '{}' to: {}, cc: {}", subject, emails, ccs);
                mailSender.send(mail);
                log.debug("Finished sending mail message with subject '{}' to: {}, cc: {}", subject, emails, ccs);
            }
        } catch (Exception e) {
            log.error("Could not send email.", e);
        }
    }

    private MimeMessage buildMail(String emails, String ccs, String subject, String template, Map<String, Object> parameters) throws MessagingException {
        MimeMessage mail = createBasicMimeMessage();
        mail.setRecipients(TO, InternetAddress.parse(emails));
        if (ccs != null) {
            mail.setRecipients(CC, InternetAddress.parse(ccs));
        }
        mail.setSubject(buildSubject(subject));
        mail.setContent(templateRenderer.render(template, parameters), "text/html; charset=utf-8");
        return mail;
    }

    private String buildSubject(String subject) {
        if (StringUtils.isNotBlank(config.getSubjectPrefix())) {
            subject = "%s %s".formatted(config.getSubjectPrefix(), subject);
        }
        return subject;
    }

    private MimeMessage createBasicMimeMessage() throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        if (StringUtils.isNotBlank(config.getReplyTo())) {
            mail.setReplyTo(InternetAddress.parse(config.getReplyTo()));
        }
        if (StringUtils.isNotBlank(config.getFrom())) {
            mail.setFrom(new InternetAddress(config.getFrom()));
        }

        return mail;
    }
}
