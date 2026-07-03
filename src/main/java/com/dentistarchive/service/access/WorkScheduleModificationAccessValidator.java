package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class WorkScheduleModificationAccessValidator extends BaseReadOnlyAccessValidator<WorkScheduleModification, WorkScheduleModificationFilter> {

    @Override
    protected WorkScheduleModificationFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return WorkScheduleModificationFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(WorkScheduleModification entity) {
        // TODO: 7/2/2026 implement the logic
        return true;
    }

    @Override
    protected Class<WorkScheduleModificationFilter> getFilterClass() {
        return WorkScheduleModificationFilter.class;
    }
}
