package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.NurseFilter;
import com.dentistarchive.search.sort.NurseSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.nurse.QNurse.nurse;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseSearchMapper extends SearchMapper<NurseFilter, NurseSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(NurseFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(nurse.id, filter.getIds())
                .eq(nurse.archived, filter.getArchived())
                .eq(nurse.clinicId, filter.getClinicId())
                .inRange(nurse.createdAt, filter.getCreatedAt())
                .inRange(nurse.hireDate, filter.getHireDate())
                .containsIgnoreCase(nurse.name, filter.getNameContains());

        if (filter.getCurrentlyEmployed() != null) {
            if (filter.getCurrentlyEmployed()) {
                builder.and(nurse.terminationDate.isNull());
            } else {
                builder.and(nurse.terminationDate.isNotNull());
            }
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    nurse.clinicId.in(
                            select(clinic.id)
                                    .from(clinic)
                                    .where(clinic.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(NurseSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(nurse.name, direction);
            case HIRE_DATE -> SortBuilder.buildOrder(nurse.hireDate, direction);
            case CREATED_AT -> SortBuilder.buildOrder(nurse.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(nurse.updatedAt, direction);
        };
    }

    @Override
    protected Class<NurseFilter> getFilterClass() {
        return NurseFilter.class;
    }

}
