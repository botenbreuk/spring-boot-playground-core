package nl.mdb.webshop.config.security.user;

import java.util.Set;

public interface RegisteredUser {

    String getUsername();

    default String getPassword() {
        return "";
    }

    Set<String> getAuthorities();

    default boolean isAccountExpired() {
        return false;
    }

    default boolean isAccountLocked() {
        return false;
    }

    default boolean isCredentialsExpired() {
        return false;
    }

    default boolean isEnabled() {
        return true;
    }
}