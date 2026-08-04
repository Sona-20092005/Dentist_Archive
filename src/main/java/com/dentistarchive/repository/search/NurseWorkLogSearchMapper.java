package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import com.dentistarchive.search.sort.NurseWorkLogSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.nurse.QNurse.nurse;
import static com.dentistarchive.entity.nurse.QNurseWorkLog.nurseWorkLog;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseWorkLogSearchMapper extends SearchMapper<NurseWorkLogFilter, NurseWorkLogSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(NurseWorkLogFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(nurseWorkLog.id, filter.getIds())
                .in(nurseWorkLog.workType, filter.getWorkTypes())
                .eq(nurseWorkLog.archived, filter.getArchived())
                .eq(nurseWorkLog.nurseId, filter.getNurseId())
                .inRange(nurseWorkLog.createdAt, filter.getCreatedAt())
                .inRange(nurseWorkLog.date, filter.getDate());

        if (filter.getClinicId() != null) {
            builder.and(
                    nurseWorkLog.nurseId.in(
                            select(nurse.id)
                                    .from(nurse)
                                    .where(nurse.clinicId.eq(filter.getClinicId()))
                    )
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    nurseWorkLog.nurseId.in(
                            select(nurse.id)
                                    .from(nurse)
                                    .where(
                                            nurse.clinicId.in(
                                                    select(clinic.id)
                                                            .from(clinic)
                                                            .where(clinic.doctorId.eq(filter.getDoctorId()))
                                            )
                                    )
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(NurseWorkLogSort sortName, SortDirection direction) {
        return switch (sortName) {
            case DATE -> SortBuilder.buildOrder(nurseWorkLog.date, direction);
            case START_TIME -> SortBuilder.buildOrder(nurseWorkLog.startTime, direction);
            case CREATED_AT -> SortBuilder.buildOrder(nurseWorkLog.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(nurseWorkLog.updatedAt, direction);
        };
    }

    @Override
    protected Class<NurseWorkLogFilter> getFilterClass() {
        return NurseWorkLogFilter.class;
    }

}
