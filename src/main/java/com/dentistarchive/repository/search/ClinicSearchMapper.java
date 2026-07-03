package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.ClinicFilter;
import com.dentistarchive.search.sort.ClinicSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicSearchMapper extends SearchMapper<ClinicFilter, ClinicSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(ClinicFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(clinic.id, filter.getIds())
                .eq(clinic.archived, filter.getArchived())
                .inRange(clinic.createdAt, filter.getCreatedAt())
                .containsIgnoreCase(clinic.name, filter.getNameContains())
                .build();
    }

    @Override
    protected Sort.Order toOrder(ClinicSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(clinic.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(clinic.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(clinic.updatedAt, direction);
        };
    }

    @Override
    protected Class<ClinicFilter> getFilterClass() {
        return ClinicFilter.class;
    }

}
