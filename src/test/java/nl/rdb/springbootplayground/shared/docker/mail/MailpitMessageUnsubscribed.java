package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class MailpitMessageUnsubscribed {

    @JsonProperty("Errors")
    private String errors;
    @JsonProperty("Header")
    private String header;
    @JsonProperty("HeaderPost")
    private String headerPost;
    @JsonProperty("Links")
    private List<String> links;
}
