package nl.rdb.springbootplayground.user;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAllUsers(GebruikerFilter filter);
}
