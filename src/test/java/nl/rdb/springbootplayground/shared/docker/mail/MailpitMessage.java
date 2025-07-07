package nl.rdb.springbootplayground.shared.docker.mail;

/**
 * <a href="https://mailpit.axllent.org/docs/api-v1/view.html#post-/api/v1/send:~:text=MESSAGES-,List%20messages,-GET%20/api/v1">REST API Message list details</a>
 */
public class MailpitMessage extends AbstractMailpitMessage {

    public boolean Read;
    public String Created;
    public int Attachments;
    // Part of body max 250 chars
    public String Snippet;
}
