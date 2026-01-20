package nl.rdb.springbootplayground.shared.docker.mail;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://mailpit.axllent.org/docs/api-v1/view.html#post-/api/v1/send:~:text=MESSAGES-,List%20messages,-GET%20/api/v1">REST API Message list details</a>
 */
@Getter
@Setter
public class MailpitMessage extends AbstractMailpitMessage {

    @JsonProperty("Read")
    private boolean read;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Attachments")
    private int attachments;
    // Part of body max 250 chars
    @JsonProperty("Snippet")
    private String snippet;
}
