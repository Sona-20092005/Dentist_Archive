package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TechnicianProcedureFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TechnicianProcedureSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.technician.QTechnician.technician;
import static com.dentistarchive.entity.technician.QTechnicianProcedure.technicianProcedure;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianProcedureSearchMapper extends SearchMapper<TechnicianProcedureFilter, TechnicianProcedureSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(TechnicianProcedureFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(technicianProcedure.id, filter.getIds())
                .eq(technicianProcedure.archived, filter.getArchived())
                .eq(technicianProcedure.technicianId, filter.getTechnicianId())
                .containsIgnoreCase(technicianProcedure.name, filter.getNameContains());

        if (filter.getDoctorId() != null) {
            builder.and(
                    technicianProcedure.technicianId.in(
                            select(technician.id)
                                    .from(technician)
                                    .where(technician.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(TechnicianProcedureSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(technicianProcedure.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(technicianProcedure.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(technicianProcedure.updatedAt, direction);
            case PRICE -> SortBuilder.buildOrder(technicianProcedure.price, direction);
        };
    }

    @Override
    protected Class<TechnicianProcedureFilter> getFilterClass() {
        return TechnicianProcedureFilter.class;
    }

}
