package nl.rdb.springbootplayground.shared.docker.mail;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class MailpitRecipient {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private String address;

    public String getEmail() {
        return this.address;
    }
}
