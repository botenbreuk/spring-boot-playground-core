package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/webui:~:text=MESSAGE-,Get%20message%20summary,-GET%20/api/v1">REST API Message details</a>
 */
@Getter
@Setter
public class MailpitMessageDetails extends AbstractMailpitMessage {

    @JsonProperty("Attachments")
    private List<MailpitMessageAttachment> attachments;
    @JsonProperty("Inline")
    private List<MailpitMessageAttachment> inline;
    @JsonProperty("ListUnsubscribe")
    private MailpitMessageUnsubscribed listUnsubscribe;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("ReturnPath")
    private String returnPath;

    @JsonProperty("HTML")
    private String html;
    @JsonProperty("Text")
    private String text;

    public String getContent() {
        return Jsoup.clean(this.html, Safelist.relaxed());
    }
}