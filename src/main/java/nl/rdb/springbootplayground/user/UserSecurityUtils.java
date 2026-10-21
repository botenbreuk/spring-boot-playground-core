package nl.rdb.springbootplayground.user;

import nl.rdb.springbootplayground.config.security.utils.SecurityUtils;
import nl.rdb.springbootplayground.shared.security.annotation.SecurityUtil;

@SecurityUtil
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
