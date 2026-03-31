package com.dentistarchive.search;

import com.dentistarchive.search.filter.BaseFilter;
import com.dentistarchive.search.filter.PaginationDto;
import com.dentistarchive.search.sort.AvailableSorts;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.SortDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class SearchMapper<F extends BaseFilter<F>, S extends AvailableSorts> {

    @SneakyThrows
    public Predicate toPredicate(Set<UUID> ids) {
        F filter = getFilterClass().getDeclaredConstructor().newInstance();
        filter.setIds(ids);
        return toPredicate(filter);
    }

    public Predicate toPredicate(F filter) {
        return toPredicate(filter, null);
    }

    public Predicate toPredicate(F filter, EntityPathBase<?> basePath) {
        if (filter == null) {
            return PredicateBuilder.builder().build();
        }
        Predicate predicate = toPredicateExceptSubFilters(filter, basePath);
        if (predicate == null) {
            predicate = PredicateBuilder.buildAlwaysTruePredicate();
        }
        if (isNotEmpty(filter.getSubFiltersAggregatedByAnd())) {
            Predicate subPredicate = filter.getSubFiltersAggregatedByAnd().stream()
                    .map(it -> toPredicate(it, basePath))
                    .reduce((p1, p2) -> PredicateBuilder.builder(p1).and(p2).build())
                    .orElse(null);
            if (subPredicate != null) {
                predicate = PredicateBuilder.builder(predicate).and(subPredicate).build();
            }
        }
        if (isNotEmpty(filter.getSubFiltersAggregatedByOr())) {
            Predicate subPredicate = filter.getSubFiltersAggregatedByOr().stream()
                    .map(it -> toPredicate(it, basePath))
                    .reduce((p1, p2) -> PredicateBuilder.builder(p1).or(p2).build())
                    .orElse(null);
            if (subPredicate != null) {
                predicate = PredicateBuilder.builder(predicate).and(subPredicate).build();
            }
        }
        if (filter.getInvertedSubFilter() != null) {
            Predicate subPredicate = this.toPredicate(filter.getInvertedSubFilter(), basePath);
            if (subPredicate != null) {
                predicate = PredicateBuilder.builder(predicate).and(subPredicate.not()).build();
            }
        }
        return predicate;
    }

    protected abstract Predicate toPredicateExceptSubFilters(F filter);

    protected Predicate toPredicateExceptSubFilters(F filter, EntityPathBase<?> basePath) {
        if (basePath == null) {
            return toPredicateExceptSubFilters(filter);
        }
        throw new NotImplementedException();
    }

    @SuppressWarnings("unchecked")
    public Sort toSort(List<? extends SortDto<?>> sorts) {
        if (CollectionUtils.isEmpty(sorts)) {
            return null;
        }
        List<Sort.Order> orders = new ArrayList<>();
        sorts.forEach(sort -> {
            Sort.Order order = toOrder((S) sort.getSort(), sort.getDirection());
            if (order == null) {
                throw new UnsupportedOperationException("Sort " + sort.getSort() + " is not supported");
            }
            orders.add(order);
        });
        return orders.isEmpty() ? null : Sort.by(orders);
    }

    public Pageable toPageable(PaginationDto pagination, Sort sort) {
        if (pagination == null) {
            return null;
        }
        return sort != null
                ? PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sort)
                : PageRequest.of(pagination.getPageNumber(), pagination.getPageSize());
    }

    protected abstract Sort.Order toOrder(S sortName, SortDirection direction);

    protected abstract Class<F> getFilterClass();
}
