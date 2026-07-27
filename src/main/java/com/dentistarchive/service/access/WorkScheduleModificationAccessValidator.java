package com.dentistarchive.service.access;

import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WorkScheduleModificationAccessValidator extends BaseReadOnlyAccessValidator<WorkScheduleModification, WorkScheduleModificationFilter> {
    final WorkScheduleRepository workScheduleRepository;

    @Override
    protected WorkScheduleModificationFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return WorkScheduleModificationFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(WorkScheduleModification entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return workScheduleRepository
                .getDoctorIdByScheduleId(entity.getScheduleId())
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<WorkScheduleModificationFilter> getFilterClass() {
        return WorkScheduleModificationFilter.class;
    }
}
