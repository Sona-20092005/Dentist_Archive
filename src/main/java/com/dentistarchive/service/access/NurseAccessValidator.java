package com.dentistarchive.service.access;

import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.search.filter.NurseFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NurseAccessValidator extends BaseReadOnlyAccessValidator<Nurse, NurseFilter> {
    final ClinicRepository clinicRepository;

    @Override
    protected NurseFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return NurseFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(Nurse entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return clinicRepository
                .getDoctorIdByClinicId(entity.getClinicId())
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<NurseFilter> getFilterClass() {
        return NurseFilter.class;
    }
}
