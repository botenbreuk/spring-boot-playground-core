package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailpitMessages {

    private int messagesCount;
    private int messagesUnread;
    private int start;
    private int total;
    private int unread;
    private List<MailpitMessage> messages;
}