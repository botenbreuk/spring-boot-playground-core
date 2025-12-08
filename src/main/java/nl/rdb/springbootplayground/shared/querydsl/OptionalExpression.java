package nl.rdb.springbootplayground.shared.querydsl;

import java.util.function.Supplier;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;

public class OptionalExpression<T extends Predicate> {

    private final Supplier<T> supplier;
    private Supplier<Boolean> check;

    private OptionalExpression(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T extends Predicate> OptionalExpression<T> of(Supplier<T> fn) {
        return new OptionalExpression<>(fn);
    }

    public OptionalExpression<T> filter(Supplier<Boolean> fn) {
        this.check = fn;
        return this;
    }

    public T orElse(T other) {
        if (check != null && !check.get()) {
            return other;
        }

        return supplier.get();
    }

    public void orQuery(BooleanBuilder builder) {
        if (builder == null) {
            throw new IllegalStateException("");
        }

        if (check != null && !check.get()) {
            return;
        }

        builder.or(supplier.get());
    }

    public void andQuery(JPQLQuery<?> query) {
        if (check != null && !check.get()) {
            return;
        }

        query.where(supplier.get());
    }
}
