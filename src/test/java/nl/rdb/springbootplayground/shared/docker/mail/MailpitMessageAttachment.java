package nl.rdb.springbootplayground.shared.docker.mail;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class MailpitMessageAttachment {

    @JsonProperty("ContentID")
    private String contentId;
    @JsonProperty("ContentType")
    private String contentType;
    @JsonProperty("FileName")
    private String fileName;
    @JsonProperty("PartID")
    private String partId;
    @JsonProperty("Size")
    private int size;
}
