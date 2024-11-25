package nl.rdb.springbootplayground.user;

/**
 * Projection for {@link User}
 */
public interface UserProj {

    String getEmail();

    String getPassword();

    String getFirstName();

    String getLastName();
}