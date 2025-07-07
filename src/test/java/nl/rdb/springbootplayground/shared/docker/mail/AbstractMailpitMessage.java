package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public abstract class AbstractMailpitMessage {

    public String ID;
    public String MessageID;
    public MailpitRecipient From;
    public List<MailpitRecipient> To;
    public List<MailpitRecipient> Cc;
    public List<MailpitRecipient> Bcc;
    public List<MailpitRecipient> ReplyTo;
    @Getter
    public String Subject;
    public List<String> Tags;
    public int Size;

    public List<String> getRecipientsTo() {
        return To.stream().map(MailpitRecipient::getEmail).collect(Collectors.toList());
    }

    public List<String> getRecipientsCc() {
        return Cc.stream().map(MailpitRecipient::getEmail).collect(Collectors.toList());
    }
}
