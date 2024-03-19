/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.mdb.webshop.shared.docker.mail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MailHogMessage {

    public String ID;
    public MailHogMessageParticipant From;
    public List<MailHogMessageParticipant> To;
    public MailHogMessageRaw Raw;
    public MailHogContent Content;

    public String getSubject() {
        try {
            return MimeUtility.decodeText(Content.Headers.Subject.get(0));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    // Returns the body as-is, to avoid being mangled by quoted-printable in case of "=" sign inside URLs.
    public String getRawContent() {
        return Content.Body;
    }

    public String getContent() {
        try {
            InputStream is = new ByteArrayInputStream(Content.Body.getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(MimeUtility.decode(is, "quoted-printable")));

            List<String> strings = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }

            strings = strings.stream().filter(v -> !StringUtils.isEmpty(v)).collect(Collectors.toList());
            return StringUtils.join(strings, "");
        } catch (IOException | MessagingException e) {
            log.error("Exceptie", e);
            return "";
        }
    }

    public List<String> getRecipientsTo() {
        return To.stream().map(MailHogMessageParticipant::getEmail).collect(Collectors.toList());
    }
}
