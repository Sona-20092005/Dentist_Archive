package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TechnicianWorkLogFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TechnicianWorkLogSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.technician.QTechnician.technician;
import static com.dentistarchive.entity.technician.QTechnicianWorkLog.technicianWorkLog;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianWorkLogSearchMapper extends SearchMapper<TechnicianWorkLogFilter, TechnicianWorkLogSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(TechnicianWorkLogFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(technicianWorkLog.id, filter.getIds())
                .eq(technicianWorkLog.archived, filter.getArchived())
                .eq(technicianWorkLog.technicianProcedureId, filter.getTechnicianProcedureId())
                .eq(technicianWorkLog.technicianId, filter.getTechnicianId())
                .eq(technicianWorkLog.isPaid, filter.getIsPaid())
                .eq(technicianWorkLog.patientId, filter.getPatientId())
                .inRange(technicianWorkLog.createdAt, filter.getCreatedAt());

        if (filter.getToothNumber() != null) {
            builder.jsonbContainsIgnoreCase(
                    technicianWorkLog.toothNumbers,
                    filter.getToothNumber().toString()
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    technicianWorkLog.technicianId.in(
                            select(technician.id)
                                    .from(technician)
                                    .where(technician.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(TechnicianWorkLogSort sortName, SortDirection direction) {
        return switch (sortName) {
            case REQUESTED_DATE -> SortBuilder.buildOrder(technicianWorkLog.requestedDate, direction);
            case COMPLETED_DATE -> SortBuilder.buildOrder(technicianWorkLog.completedDate, direction);
            case CREATED_AT -> SortBuilder.buildOrder(technicianWorkLog.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(technicianWorkLog.updatedAt, direction);
        };
    }

    @Override
    protected Class<TechnicianWorkLogFilter> getFilterClass() {
        return TechnicianWorkLogFilter.class;
    }

}
