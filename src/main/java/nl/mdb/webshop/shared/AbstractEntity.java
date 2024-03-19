package nl.mdb.webshop.shared;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Setter;

import org.springframework.data.domain.Persistable;

@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
