package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.NurseCompensationTypeChangeFilter;
import com.dentistarchive.search.sort.NurseCompensationTypeChangeSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.nurse.QNurse.nurse;
import static com.dentistarchive.entity.nurse.QNurseCompensationTypeChange.nurseCompensationTypeChange;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseCompensationTypeChangeSearchMapper extends SearchMapper<NurseCompensationTypeChangeFilter, NurseCompensationTypeChangeSort> {

    // TODO: 9/10/2026 add yearMonth filtration
    @Override
    protected Predicate toPredicateExceptSubFilters(NurseCompensationTypeChangeFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(nurseCompensationTypeChange.id, filter.getIds())
                .in(nurseCompensationTypeChange.compensationType, filter.getCompensationTypes())
                .eq(nurseCompensationTypeChange.nurseId, filter.getNurseId());

        if (filter.getDoctorId() != null) {
            builder.and(
                    nurseCompensationTypeChange.nurseId.in(
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
    protected Sort.Order toOrder(NurseCompensationTypeChangeSort sortName, SortDirection direction) {
        return switch (sortName) {
            case YEARMONTH -> SortBuilder.buildOrder(nurseCompensationTypeChange.effectiveFrom, direction);
            case CREATED_AT -> SortBuilder.buildOrder(nurseCompensationTypeChange.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(nurseCompensationTypeChange.updatedAt, direction);
        };
    }

    @Override
    protected Class<NurseCompensationTypeChangeFilter> getFilterClass() {
        return NurseCompensationTypeChangeFilter.class;
    }

}
