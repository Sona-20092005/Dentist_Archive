package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.SupplierSort;
import com.dentistarchive.search.filter.SupplierFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.material.QSupplier.supplier;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierSearchMapper extends SearchMapper<SupplierFilter, SupplierSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(SupplierFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(supplier.id, filter.getIds())
                .eq(supplier.archived, filter.getArchived())
                .eq(supplier.doctorId, filter.getDoctorId())
                .containsIgnoreCase(supplier.name, filter.getNameContains())
                .jsonbContainsIgnoreCase(supplier.phones, filter.getPhoneContains());

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(SupplierSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(supplier.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(supplier.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(supplier.updatedAt, direction);
        };
    }

    @Override
    protected Class<SupplierFilter> getFilterClass() {
        return SupplierFilter.class;
    }

}
