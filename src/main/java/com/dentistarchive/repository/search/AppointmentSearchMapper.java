package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.AppointmentFilter;
import com.dentistarchive.search.sort.AppointmentSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.schedule.QAppointment.appointment;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentSearchMapper extends SearchMapper<AppointmentFilter, AppointmentSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(AppointmentFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(appointment.id, filter.getIds())
                .in(appointment.appointmentStatus, filter.getStatuses())
                .eq(appointment.archived, filter.getArchived())
                .eq(appointment.doctorId, filter.getDoctorId())
                .eq(appointment.patientId, filter.getPatientId())
                .eq(appointment.nurseId, filter.getNurseId())
                .inRange(appointment.createdAt, filter.getCreatedAt())
                .build();
    }

    @Override
    protected Sort.Order toOrder(AppointmentSort sortName, SortDirection direction) {
        return switch (sortName) {
            case DATE -> SortBuilder.buildOrder(appointment.date, direction);
            case CREATED_AT -> SortBuilder.buildOrder(appointment.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(appointment.updatedAt, direction);
        };
    }

    @Override
    protected Class<AppointmentFilter> getFilterClass() {
        return AppointmentFilter.class;
    }

}
