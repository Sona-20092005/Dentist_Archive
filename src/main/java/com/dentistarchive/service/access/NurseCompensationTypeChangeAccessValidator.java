package com.dentistarchive.service.access;

import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.repository.NurseRepository;
import com.dentistarchive.search.filter.NurseCompensationTypeChangeFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NurseCompensationTypeChangeAccessValidator extends BaseReadOnlyAccessValidator<NurseCompensationTypeChange, NurseCompensationTypeChangeFilter> {
    final ClinicRepository clinicRepository;
    final NurseRepository nurseRepository;

    @Override
    protected NurseCompensationTypeChangeFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return NurseCompensationTypeChangeFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(NurseCompensationTypeChange entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return nurseRepository
                .getClinicIdByNurseId(entity.getNurseId())
                .flatMap(clinicRepository::getDoctorIdByClinicId)
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<NurseCompensationTypeChangeFilter> getFilterClass() {
        return NurseCompensationTypeChangeFilter.class;
    }
}
