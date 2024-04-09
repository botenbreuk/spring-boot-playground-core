/*
 * Copyright (c) 2021. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.shared.docker.mail;

import java.util.List;

public class MailHogMessageRaw {

    public String From;
    public List<String> To;
    public String Data;
    public String Helo;
}
