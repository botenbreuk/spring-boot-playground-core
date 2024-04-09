package nl.rdb.springbootplayground.user;

public enum UserRole {
    ADMIN,
    USER;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
