package nl.rdb.springbootplayground.user;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public record UserRepositoryImpl(EntityManager em) implements UserRepositoryCustom {

    private static final Class<User> USER = User.class;

    @Override
    public List<User> findAllUsers(GebruikerFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(USER);
        Root<User> root = cr.from(USER);

        List<Predicate> predicates = new ArrayList<>();
        if (isNotEmpty(filter.getEmail())) {
            predicates.add(cb.like(root.get(User_.EMAIL), "%%%s%%".formatted(filter.getEmail())));
        }

        if (isNotEmpty(filter.getPhonenumber())) {
            predicates.add(cb.like(root.get(User_.PHONE), "%%%s%%".formatted(filter.getPhonenumber())));
        }

        cr.select(root).where(predicates.toArray(Predicate[]::new));

        return em.createQuery(cr).getResultList();
    }
}
