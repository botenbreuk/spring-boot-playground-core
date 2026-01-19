package nl.rdb.springbootplayground.shared.querydsl;

import static nl.rdb.springbootplayground.shared.querydsl.FreeTextPredicateBuilder.build;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;

public abstract class AbstractQueryDslRepository extends QuerydslRepositorySupport {

    protected AbstractQueryDslRepository(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

    protected <T> Page<T> fetchPage(JPQLQuery<T> query, Pageable pageable) {
        QueryResults<T> resultSet = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetchResults();
        return new PageImpl<>(resultSet.getResults(), pageable, resultSet.getTotal());
    }

    protected Predicate textMatches(String searchArgument, StringPath searchField) {
        return build(searchArgument, searchField);
    }

    protected <T> DateExpression<LocalDate> castToDate(T date) {
        return Expressions.dateTemplate(LocalDate.class, "DATE({0})", date);
    }
}