package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.RoleFilter;
import com.dentistarchive.search.sort.RoleSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QRole.role;

@Component
public class RoleSearchMapper extends SearchMapper<RoleFilter, RoleSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(RoleFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(role.id, filter.getIds())
                .eq(role.clientRole, filter.getClientRole())
                .in(role.scope, filter.getScopes())
                .in(role.code, filter.getCodes())
                .build();
    }

    @Override
    protected Sort.Order toOrder(RoleSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(role.name, direction);
        };
    }

    @Override
    protected Class<RoleFilter> getFilterClass() {
        return RoleFilter.class;
    }
}
