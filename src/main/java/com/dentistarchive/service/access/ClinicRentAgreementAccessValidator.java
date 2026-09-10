package com.dentistarchive.service.access;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClinicRentAgreementAccessValidator extends BaseReadOnlyAccessValidator<ClinicRentAgreement, ClinicRentAgreementFilter> {
    final ClinicRepository clinicRepository;

    @Override
    protected ClinicRentAgreementFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return ClinicRentAgreementFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(ClinicRentAgreement entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return clinicRepository
                .getDoctorIdByClinicId(entity.getClinicId())
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<ClinicRentAgreementFilter> getFilterClass() {
        return ClinicRentAgreementFilter.class;
    }
}
