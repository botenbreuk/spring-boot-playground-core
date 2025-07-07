package nl.rdb.springbootplayground.user;

public enum UserRole {
    ADMIN,
    USER,
    SYSTEM;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
