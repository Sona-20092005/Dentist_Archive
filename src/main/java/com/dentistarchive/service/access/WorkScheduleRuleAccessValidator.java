package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class WorkScheduleRuleAccessValidator extends BaseReadOnlyAccessValidator<WorkScheduleRule, WorkScheduleRuleFilter> {

    @Override
    protected WorkScheduleRuleFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return WorkScheduleRuleFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(WorkScheduleRule entity) {
        // TODO: 6/15/2026 add logic here
//        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();
//
//        return details.getUserId().equals(entity.getDoctorId());
        return true;
    }

    @Override
    protected Class<WorkScheduleRuleFilter> getFilterClass() {
        return WorkScheduleRuleFilter.class;
    }
}
