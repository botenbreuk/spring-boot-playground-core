package nl.mdb.webshop.user;

import nl.mdb.webshop.config.security.utils.SecurityUtils;

import org.springframework.stereotype.Component;

@Component
public class UserSecurityUtils {

    public boolean create() {
        return SecurityUtils.isAdmin();
    }

    public boolean update() {
        return SecurityUtils.isAdmin();
    }

    public boolean delete() {
        return SecurityUtils.isAdmin();
    }
}
