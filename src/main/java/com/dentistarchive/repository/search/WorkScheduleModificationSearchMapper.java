package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.WorkScheduleModificationSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.schedule.QWorkSchedule.workSchedule;
import static com.dentistarchive.entity.schedule.QWorkScheduleModification.workScheduleModification;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleModificationSearchMapper extends SearchMapper<WorkScheduleModificationFilter, WorkScheduleModificationSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(WorkScheduleModificationFilter filter) {
        PredicateBuilder builder =  PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(workScheduleModification.id, filter.getIds())
                .in(workScheduleModification.modificationType, filter.getModificationTypes())
                .in(workScheduleModification.workSessionType, filter.getWorkSessionTypes())
                .eq(workScheduleModification.archived, filter.getArchived())
                .eq(workScheduleModification.scheduleId, filter.getScheduleId())
                .inRange(workScheduleModification.date, filter.getDate())
                .inRange(workScheduleModification.createdAt, filter.getCreatedAt());

        if (filter.getDoctorId() != null) {
            builder.and(
                    workScheduleModification.scheduleId.in(
                            select(workSchedule.id)
                                    .from(workSchedule)
                                    .where(workSchedule.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        if (filter.getClinicId() != null) {
            builder.and(
                    workScheduleModification.scheduleId.in(
                            select(workSchedule.id)
                                    .from(workSchedule)
                                    .where(workSchedule.clinicId.eq(filter.getClinicId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(WorkScheduleModificationSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(workScheduleModification.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(workScheduleModification.updatedAt, direction);
            case DATE -> SortBuilder.buildOrder(workScheduleModification.date, direction);
            case START_TIME -> SortBuilder.buildOrder(workScheduleModification.startTime, direction);
            case MODIFICATION_TYPE -> SortBuilder.buildOrder(workScheduleModification.modificationTypeSortOrder, direction);
            case WORK_SESSION_TYPE -> SortBuilder.buildOrder(workScheduleModification.workSessionTypeSortOrder, direction);
        };
    }

    @Override
    protected Class<WorkScheduleModificationFilter> getFilterClass() {
        return WorkScheduleModificationFilter.class;
    }

}
