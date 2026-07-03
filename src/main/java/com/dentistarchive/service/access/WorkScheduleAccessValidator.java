package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.search.filter.WorkScheduleFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class WorkScheduleAccessValidator extends BaseReadOnlyAccessValidator<WorkSchedule, WorkScheduleFilter> {

    @Override
    protected WorkScheduleFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return WorkScheduleFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(WorkSchedule entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }

    @Override
    protected Class<WorkScheduleFilter> getFilterClass() {
        return WorkScheduleFilter.class;
    }
}
