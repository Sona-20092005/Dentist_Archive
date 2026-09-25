package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.RentOvertimeFilter;
import com.dentistarchive.search.sort.RentOvertimeSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.rent.QRentOvertime.rentOvertime;
import static com.dentistarchive.entity.schedule.QWorkSchedule.workSchedule;
import static com.dentistarchive.entity.schedule.QWorkScheduleModification.workScheduleModification;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentOvertimeSearchMapper extends SearchMapper<RentOvertimeFilter, RentOvertimeSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(RentOvertimeFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(rentOvertime.id, filter.getIds())
                .eq(rentOvertime.locked, filter.getLocked())
                .eq(rentOvertime.workScheduleModificationId, filter.getWorkScheduleModificationId())
                .inRange(rentOvertime.createdAt, filter.getCreatedAt());

        if (filter.getDate() != null) {
            builder.and(
                    rentOvertime.workScheduleModificationId.in(
                            select(workScheduleModification.id)
                                    .from(workScheduleModification)
                                    .where(
                                            PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                                                    .inRange(
                                                            workScheduleModification.date,
                                                            filter.getDate()
                                                    )
                                                    .build()
                                    )
                    )
            );
        }

        if (filter.getClinicId() != null) {
            builder.and(
                    rentOvertime.workScheduleModificationId.in(
                            select(workScheduleModification.id)
                                    .from(workScheduleModification)
                                    .where(
                                            workScheduleModification.scheduleId.in(
                                                    select(workSchedule.id)
                                                            .from(workSchedule)
                                                            .where(
                                                                    workSchedule.clinicId.eq(filter.getClinicId())
                                                            )
                                            )
                                    )
                    )
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    rentOvertime.workScheduleModificationId.in(
                            select(workScheduleModification.id)
                                    .from(workScheduleModification)
                                    .where(
                                            workScheduleModification.scheduleId.in(
                                                    select(workSchedule.id)
                                                            .from(workSchedule)
                                                            .where(
                                                                    workSchedule.doctorId.eq(filter.getDoctorId())
                                                            )
                                            )
                                    )
                    )
            );
        }



        return builder.build();
    }

    // TODO: 9/15/2026 add sorting by date 
    @Override
    protected Sort.Order toOrder(RentOvertimeSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(rentOvertime.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(rentOvertime.updatedAt, direction);
        };
    }

    @Override
    protected Class<RentOvertimeFilter> getFilterClass() {
        return RentOvertimeFilter.class;
    }

}
