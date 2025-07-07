package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

/**
 * <a href="https://mailpit.axllent.org/docs/api-v1/view.html#get-/api/v1/webui:~:text=MESSAGE-,Get%20message%20summary,-GET%20/api/v1">REST API Message details</a>
 */
public class MailpitMessageDetails extends AbstractMailpitMessage {

    public List<MailpitMessageAttachment> Attachments;
    public List<MailpitMessageAttachment> Inline;
    public MailpitMessageUnsubscribed ListUnsubscribe;
    public String Date;
    public String ReturnPath;

    public String HTML;
    public String Text;

    public String getContent() {
        return this.HTML;
    }
}