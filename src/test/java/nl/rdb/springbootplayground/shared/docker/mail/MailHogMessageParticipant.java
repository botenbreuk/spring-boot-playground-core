/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker.mail;

public class MailHogMessageParticipant {

    public String Mailbox;
    public String Domain;

    public String getEmail() {
        return String.format("%s@%s", Mailbox, Domain);
    }
}
