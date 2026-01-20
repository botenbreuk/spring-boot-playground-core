package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public abstract class AbstractMailpitMessage {

    @JsonProperty("ID")
    private String id;
    @JsonProperty("MessageID")
    private String messageId;
    @JsonProperty("From")
    private MailpitRecipient from;
    @JsonProperty("To")
    private List<MailpitRecipient> to;
    @JsonProperty("Cc")
    private List<MailpitRecipient> cc;
    @JsonProperty("Bcc")
    private List<MailpitRecipient> bcc;
    @JsonProperty("ReplyTo")
    private List<MailpitRecipient> replyTo;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Tags")
    private List<String> tags;
    @JsonProperty("Size")
    private int size;

    public List<String> getRecipientsTo() {
        return this.to.stream().map(MailpitRecipient::getEmail).toList();
    }

    public List<String> getRecipientsCc() {
        return this.cc.stream().map(MailpitRecipient::getEmail).toList();
    }
}
