package nl.rdb.springbootplayground.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //    Optional<User> findByEmail(String username);

    UserProj findByLastName(String lastName);

    UserClassProj findJeMoederByEmail(String email);
}
