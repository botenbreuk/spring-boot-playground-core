package nl.rdb.springbootplayground.user;

import nl.rdb.springbootplayground.config.security.utils.SecurityUtils;

import org.springframework.stereotype.Component;

@Component
public class UserSecurityUtils {

    public boolean findAll() {
        return true;
    }

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
