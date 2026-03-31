package com.dentistarchive.search;

import com.dentistarchive.search.filter.RangeFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.lang.Boolean.TRUE;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PredicateBuilder {

    private static final Predicate ALWAYS_TRUE = new BooleanBuilder(Expressions.asBoolean(true).isTrue());
    private static final Predicate ALWAYS_FALSE = new BooleanBuilder(Expressions.asBoolean(true).isTrue());

    final Aggregation aggregation;

    BooleanBuilder builder;

    public static PredicateBuilder builder() {
        return builder(Aggregation.AND);
    }

    public static PredicateBuilder builder(Aggregation aggregation) {
        return new PredicateBuilder(aggregation);
    }

    public static PredicateBuilder builder(Predicate predicate) {
        return builder(predicate, Aggregation.AND);
    }

    public static PredicateBuilder builder(Predicate predicate, Aggregation aggregation) {
        if ((ALWAYS_TRUE.equals(predicate) && Aggregation.AND.equals(aggregation))
                || (ALWAYS_FALSE.equals(predicate) && Aggregation.OR.equals(aggregation))) {
            return new PredicateBuilder(aggregation);
        }
        return new PredicateBuilder(aggregation, new BooleanBuilder(predicate));
    }

    public static Predicate buildAlwaysTruePredicate() {
        return ALWAYS_TRUE;
    }

    public static Predicate buildAlwaysFalsePredicate() {
        return ALWAYS_FALSE;
    }

    public Predicate build() {
        return builder != null ? builder.getValue() : ALWAYS_TRUE;
    }

    public <T> PredicateBuilder eq(SimpleExpression<T> attribute, T value) {
        return eq(attribute, value, Objects::nonNull);
    }

    public <T> PredicateBuilder notEq(SimpleExpression<T> attribute, T value) {
        return notEq(attribute, value, Objects::nonNull);
    }

    public <T> PredicateBuilder eq(SimpleExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.eq(value));
        }
        return this;
    }

    public <T> PredicateBuilder notEq(SimpleExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.eq(value).not());
        }
        return this;
    }

    public PredicateBuilder eq(SimpleExpression<UUID> attribute, String value) {
        try {
            addBooleanExpression(attribute.eq(UUID.fromString(value)));
            return this;
        } catch (IllegalArgumentException e) {
            return this;
        }
    }

    public <T> PredicateBuilder in(SimpleExpression<T> attribute, Collection<T> value) {
        return in(attribute, value, v -> !CollectionUtils.isEmpty(v));
    }

    public <T> PredicateBuilder in(SimpleExpression<T> attribute, Collection<T> value, Function<Collection<T>, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.in(value));
        }
        return this;
    }

    public <T extends Number & Comparable<T>> PredicateBuilder inRange(
            NumberPath<T> attribute,
            RangeFilter<T> range
    ) {
        return inRange(attribute, range, Objects::nonNull);
    }

    public <T extends Number & Comparable<T>> PredicateBuilder inRange(
            NumberPath<T> attribute,
            RangeFilter<T> range,
            Function<RangeFilter<T>, Boolean> validator
    ) {
        if (TRUE.equals(validator.apply(range))) {
            if (range.getMin() != null) {
                addBooleanExpression(attribute.goe(range.getMin()));
            }
            if (range.getMax() != null) {
                addBooleanExpression(attribute.loe(range.getMax()));
            }
        }
        return this;
    }

    public <T extends Comparable<T>> PredicateBuilder inRange(
            ComparableExpression<T> attribute,
            RangeFilter<T> range
    ) {
        return inRange(attribute, range, Objects::nonNull);
    }

    public <T extends Comparable<T>> PredicateBuilder inRange(
            ComparableExpression<T> attribute,
            RangeFilter<T> range,
            Function<RangeFilter<T>, Boolean> validator
    ) {
        if (TRUE.equals(validator.apply(range))) {
            if (range.getMin() != null) {
                addBooleanExpression(attribute.goe(range.getMin()));
            }
            if (range.getMax() != null) {
                addBooleanExpression(attribute.loe(range.getMax()));
            }
        }
        return this;
    }

    public <T extends Number & Comparable<?>> PredicateBuilder greaterThan(NumberPath<T> attribute, T value) {
        return greaterThan(attribute, value, Objects::nonNull);
    }

    public <T extends Number & Comparable<?>> PredicateBuilder greaterThan(
            NumberPath<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.gt(value));
        }
        return this;
    }

    public <T extends Comparable<?>> PredicateBuilder greaterThan(ComparableExpression<T> attribute, T value) {
        return greaterThan(attribute, value, Objects::nonNull);
    }

    public <T extends Comparable<?>> PredicateBuilder greaterThan(
            ComparableExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.gt(value));
        }
        return this;
    }

    public <T extends Number & Comparable<?>> PredicateBuilder greaterThanOrEquals(NumberPath<T> attribute, T value) {
        return greaterThanOrEquals(attribute, value, Objects::nonNull);
    }

    public <T extends Number & Comparable<?>> PredicateBuilder greaterThanOrEquals(
            NumberPath<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.goe(value));
        }
        return this;
    }

    public <T extends Comparable<?>> PredicateBuilder greaterThanOrEquals(ComparableExpression<T> attribute, T value) {
        return greaterThanOrEquals(attribute, value, Objects::nonNull);
    }

    public <T extends Comparable<?>> PredicateBuilder greaterThanOrEquals(
            ComparableExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.goe(value));
        }
        return this;
    }

    public <T extends Number & Comparable<?>> PredicateBuilder lessThan(NumberPath<T> attribute, T value) {
        return lessThan(attribute, value, Objects::nonNull);
    }

    public <T extends Number & Comparable<?>> PredicateBuilder lessThan(
            NumberPath<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.lt(value));
        }
        return this;
    }

    public <T extends Comparable<?>> PredicateBuilder lessThan(ComparableExpression<T> attribute, T value) {
        return lessThan(attribute, value, Objects::nonNull);
    }

    public <T extends Comparable<?>> PredicateBuilder lessThan(
            ComparableExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.lt(value));
        }
        return this;
    }

    public <T extends Number & Comparable<?>> PredicateBuilder lessThanOrEquals(NumberPath<T> attribute, T value) {
        return lessThanOrEquals(attribute, value, Objects::nonNull);
    }

    public <T extends Number & Comparable<?>> PredicateBuilder lessThanOrEquals(
            NumberPath<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.loe(value));
        }
        return this;
    }

    public <T extends Comparable<?>> PredicateBuilder lessThanOrEquals(ComparableExpression<T> attribute, T value) {
        return lessThanOrEquals(attribute, value, Objects::nonNull);
    }

    public <T extends Comparable<?>> PredicateBuilder lessThanOrEquals(
            ComparableExpression<T> attribute, T value, Function<T, Boolean> validator) {
        if (TRUE.equals(validator.apply(value))) {
            addBooleanExpression(attribute.loe(value));
        }
        return this;
    }

    public <T extends Comparable<?>> PredicateBuilder dateRange(DateTimePath<T> attribute, T start, T end) {
        if (start != null && end != null) {
            addBooleanExpression(attribute.between(start, end));
        } else if (start != null) {
            addBooleanExpression(attribute.goe(start));
        } else if (end != null) {
            addBooleanExpression(attribute.loe(end));
        }
        return this;
    }

    public PredicateBuilder containsIgnoreCase(StringExpression attribute, String value) {
        if (Objects.nonNull(value)) {
            addBooleanExpression(attribute.containsIgnoreCase(value));
        }
        return this;
    }

    public <T> PredicateBuilder isNull(SimpleExpression<T> attribute) {
        addBooleanExpression(attribute.isNull());
        return this;
    }

    public <T> PredicateBuilder isNotNull(SimpleExpression<T> attribute) {
        addBooleanExpression(attribute.isNotNull());
        return this;
    }

    public PredicateBuilder isTrue(BooleanExpression booleanExpression) {
        addBooleanExpression(booleanExpression);
        return this;
    }

    public PredicateBuilder isFalse(BooleanExpression booleanExpression) {
        addBooleanExpression(booleanExpression.not());
        return this;
    }

    public PredicateBuilder and(Predicate predicate) {
        if (ALWAYS_TRUE.equals(predicate)) {
            return this;
        }
        if (builder == null) {
            builder = new BooleanBuilder(predicate);
            return this;
        }
        builder.and(predicate);
        return this;
    }

    public PredicateBuilder or(Predicate predicate) {
        if (ALWAYS_FALSE.equals(predicate)) {
            return this;
        }
        if (builder == null) {
            builder = new BooleanBuilder(predicate);
            return this;
        }
        builder.or(predicate);
        return this;
    }

    private void addBooleanExpression(BooleanExpression expression) {
        if (builder == null) {
            builder = new BooleanBuilder(expression);
            return;
        }
        switch (aggregation) {
            case AND -> builder.and(expression);
            case OR -> builder.or(expression);
        }
    }

    public enum Aggregation {
        AND, OR;
    }
}

