package nl.rdb.springbootplayground.shared.querydsl;

import static lombok.AccessLevel.PRIVATE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import lombok.Getter;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;

public final class OptionalParamsQueryBuilder<T> {

    private final JPQLQuery<T> query;
    private final List<PredicateBuilder> builders = new ArrayList<>();

    private OptionalParamsQueryBuilder(JPQLQuery<T> query) {
        this.query = query;
    }

    public static <T> OptionalParamsQueryBuilder<T> query(JPQLQuery<T> query) {
        return new OptionalParamsQueryBuilder<>(query);
    }

    public OptionalParamsQueryBuilder<T> and(Predicate predicate) {
        this.query.where(predicate);
        return this;
    }

    public OptionalParamsQueryBuilder<T> and(UnaryOperator<PredicateBuilder> builder) {
        this.builders.add(builder.apply(new PredicateBuilder()));
        return this;
    }

    public void build() {
        builders.stream()
                .filter(builder -> builder.getCheck().get())
                .forEach(builder -> this.query.where(builder.getSupplier().get()));
    }

    @Getter(PRIVATE)
    public static class PredicateBuilder {

        private Supplier<Predicate> supplier;
        private Supplier<Boolean> check = () -> true;

        public PredicateBuilder predicate(Supplier<Predicate> fn) {
            this.supplier = fn;
            return this;
        }

        public PredicateBuilder filter(Supplier<Boolean> fn) {
            this.check = fn;
            return this;
        }
    }
}
