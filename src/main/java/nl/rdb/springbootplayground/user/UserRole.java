package nl.rdb.springbootplayground.user;

import nl.rdb.springbootplayground.enums.EnumLabel;

public enum UserRole implements EnumLabel {
    ADMIN("Admin"),
    USER("User"),
    SYSTEM("System");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
