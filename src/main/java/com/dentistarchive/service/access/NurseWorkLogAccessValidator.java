package com.dentistarchive.service.access;

import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.repository.NurseRepository;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NurseWorkLogAccessValidator extends BaseReadOnlyAccessValidator<NurseWorkLog, NurseWorkLogFilter> {
    final ClinicRepository clinicRepository;
    final NurseRepository nurseRepository;

    @Override
    protected NurseWorkLogFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return NurseWorkLogFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(NurseWorkLog entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return nurseRepository
                .getClinicIdByNurseId(entity.getNurseId())
                .flatMap(clinicRepository::getDoctorIdByClinicId)
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<NurseWorkLogFilter> getFilterClass() {
        return NurseWorkLogFilter.class;
    }
}
