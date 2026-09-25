package com.dentistarchive.service.access;

import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.search.filter.RentOvertimeFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RentOvertimeAccessValidator extends BaseReadOnlyAccessValidator<RentOvertime, RentOvertimeFilter> {
    final WorkScheduleModificationRepository workScheduleModificationRepository;
    final WorkScheduleRepository workScheduleRepository;

    @Override
    protected RentOvertimeFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return RentOvertimeFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(RentOvertime entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return workScheduleModificationRepository
                .getScheduleIdByModificationId(entity.getWorkScheduleModificationId())
                .flatMap(workScheduleRepository::getDoctorIdByScheduleId)
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<RentOvertimeFilter> getFilterClass() {
        return RentOvertimeFilter.class;
    }
}
