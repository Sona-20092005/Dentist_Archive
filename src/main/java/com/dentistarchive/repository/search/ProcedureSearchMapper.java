package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.ProcedureFilter;
import com.dentistarchive.search.sort.ProcedureSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.pricelist.QProcedure.procedure;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcedureSearchMapper extends SearchMapper<ProcedureFilter, ProcedureSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(ProcedureFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(procedure.id, filter.getIds())
                .eq(procedure.archived, filter.isArchived())
                .containsIgnoreCase(procedure.name, filter.getNameContains())
                .build();
    }

    @Override
    protected Sort.Order toOrder(ProcedureSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(procedure.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(procedure.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(procedure.updatedAt, direction);
        };
    }

    @Override
    protected Class<ProcedureFilter> getFilterClass() {
        return ProcedureFilter.class;
    }

}
