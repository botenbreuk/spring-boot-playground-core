package nl.rdb.springbootplayground.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query("""
        SELECT user
        FROM User user
        WHERE user.email = :credential
            OR user.username = :credential
        """)
    Optional<User> findByEmailOrUsername(String credential);
}
