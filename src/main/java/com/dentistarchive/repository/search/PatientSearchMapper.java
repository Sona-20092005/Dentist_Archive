package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.PatientFilter;
import com.dentistarchive.search.sort.PatientSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QPatient.patient;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientSearchMapper extends SearchMapper<PatientFilter, PatientSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(PatientFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(patient.id, filter.getIds())
                .eq(patient.archived, filter.getArchived())
                .containsIgnoreCase(patient.name, filter.getNameContains())
                .build();
    }

    @Override
    protected Sort.Order toOrder(PatientSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(patient.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(patient.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(patient.updatedAt, direction);
        };
    }

    @Override
    protected Class<PatientFilter> getFilterClass() {
        return PatientFilter.class;
    }

}
