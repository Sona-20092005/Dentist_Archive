package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.search.filter.AppointmentFilter;
import org.springframework.stereotype.Component;

@Component
public class AppointmentAccessValidator extends BaseReadOnlyAccessValidator<Appointment, AppointmentFilter> {

    @Override
    protected AppointmentFilter buildAccessControlFilter() {
//        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();
//
//        return  AppointmentFilter.builder().
//                doctorId(details.getUserId()).
//                build();
        return AppointmentFilter.builder().build();
    }

    @Override
    protected boolean hasAccess(Appointment entity) {
//        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();
//
//        return details.getUserId().equals(entity.getDoctorId());
        return true;
    }

    @Override
    protected Class<AppointmentFilter> getFilterClass() {
        return AppointmentFilter.class;
    }
}
