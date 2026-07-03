package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.WorkScheduleFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.WorkScheduleSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.schedule.QWorkSchedule.workSchedule;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleSearchMapper extends SearchMapper<WorkScheduleFilter, WorkScheduleSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(WorkScheduleFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(workSchedule.id, filter.getIds())
                .eq(workSchedule.archived, filter.getArchived())
                .eq(workSchedule.doctorId, filter.getDoctorId())
                .eq(workSchedule.clinicId, filter.getClinicId())
                .inRange(workSchedule.createdAt, filter.getCreatedAt())
                .build();
    }

    @Override
    protected Sort.Order toOrder(WorkScheduleSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(workSchedule.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(workSchedule.updatedAt, direction);
        };
    }

    @Override
    protected Class<WorkScheduleFilter> getFilterClass() {
        return WorkScheduleFilter.class;
    }

}
