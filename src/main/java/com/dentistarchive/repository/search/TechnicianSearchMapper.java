package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TechnicianFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TechnicianSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.technician.QTechnician.technician;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianSearchMapper extends SearchMapper<TechnicianFilter, TechnicianSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(TechnicianFilter filter) {

        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(technician.id, filter.getIds())
                .eq(technician.archived, filter.getArchived())
                .eq(technician.doctorId, filter.getDoctorId())
                .inRange(technician.createdAt, filter.getCreatedAt())
                .containsIgnoreCase(technician.name, filter.getNameContains())
                .containsIgnoreCase(technician.address, filter.getAddressContains())
                .build();
    }

    @Override
    protected Sort.Order toOrder(TechnicianSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(technician.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(technician.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(technician.updatedAt, direction);
        };
    }

    @Override
    protected Class<TechnicianFilter> getFilterClass() {
        return TechnicianFilter.class;
    }

}
