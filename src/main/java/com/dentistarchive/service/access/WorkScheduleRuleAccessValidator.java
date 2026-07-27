package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WorkScheduleRuleAccessValidator extends BaseReadOnlyAccessValidator<WorkScheduleRule, WorkScheduleRuleFilter> {
    final WorkScheduleRepository workScheduleRepository;

    @Override
    protected WorkScheduleRuleFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return WorkScheduleRuleFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(WorkScheduleRule entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return workScheduleRepository
                .getDoctorIdByScheduleId(entity.getScheduleId())
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<WorkScheduleRuleFilter> getFilterClass() {
        return WorkScheduleRuleFilter.class;
    }
}
