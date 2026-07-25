package nl.rdb.springbootplayground.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, I> extends JpaRepository<T, I> {

    default T findOne(I id) {
        return findById(id).orElse(null);
    }
}
