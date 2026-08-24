package com.dentistarchive.service.access;

import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.repository.NurseRepository;
import com.dentistarchive.search.filter.NursePayrollFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NursePayrollAccessValidator extends BaseReadOnlyAccessValidator<NursePayroll, NursePayrollFilter> {
    final ClinicRepository clinicRepository;
    final NurseRepository nurseRepository;

    @Override
    protected NursePayrollFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return NursePayrollFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(NursePayroll entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return nurseRepository
                .getClinicIdByNurseId(entity.getNurseId())
                .flatMap(clinicRepository::getDoctorIdByClinicId)
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<NursePayrollFilter> getFilterClass() {
        return NursePayrollFilter.class;
    }
}
