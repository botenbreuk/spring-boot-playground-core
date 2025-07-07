package nl.rdb.springbootplayground.shared.docker.mail;

public class MailpitRecipient {

    public String Name;
    public String Address;

    public String getEmail() {
        return this.Address;
    }
}
