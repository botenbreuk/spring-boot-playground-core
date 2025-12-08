package nl.rdb.springbootplayground.user;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import nl.rdb.springbootplayground.shared.querydsl.AbstractQueryDslRepository;
import nl.rdb.springbootplayground.shared.querydsl.OptionalExpression;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.JPQLQuery;

@Component
public class UserRepositoryImpl extends AbstractQueryDslRepository implements UserRepositoryCustom {

    private static final QUser USER = QUser.user;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<User> findAllUsers(GebruikerFilter filter) {
        JPQLQuery<User> query = from(USER);

        OptionalExpression.of(() -> textMatches(filter.getEmail(), USER.email))
                .filter(() -> isNotEmpty(filter.getEmail()))
                .andQuery(query);

        OptionalExpression.of(() -> textMatches(filter.getPhonenumber(), USER.phone))
                .filter(() -> isNotEmpty(filter.getPhonenumber()))
                .andQuery(query);

        return query.fetchResults().getResults();
    }
}
