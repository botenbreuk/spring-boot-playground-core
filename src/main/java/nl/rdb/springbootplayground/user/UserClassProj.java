package nl.rdb.springbootplayground.user;

/**
 * Projection for {@link User}
 */
public record UserClassProj(String email, String password, String firstName, String lastName) {}