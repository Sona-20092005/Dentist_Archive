package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.search.filter.AppointmentFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppointmentAccessValidator extends BaseReadOnlyAccessValidator<Appointment, AppointmentFilter> {
    final WorkScheduleRepository workScheduleRepository;

    @Override
    protected AppointmentFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return AppointmentFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(Appointment entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return workScheduleRepository
                .getDoctorIdByScheduleId(entity.getScheduleId())
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<AppointmentFilter> getFilterClass() {
        return AppointmentFilter.class;
    }
}
