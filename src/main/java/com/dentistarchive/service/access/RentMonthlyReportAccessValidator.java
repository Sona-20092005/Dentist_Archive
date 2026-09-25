package com.dentistarchive.service.access;

import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.repository.ClinicRentAgreementRepository;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.search.filter.RentMonthlyReportFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RentMonthlyReportAccessValidator extends BaseReadOnlyAccessValidator<RentMonthlyReport, RentMonthlyReportFilter> {
    private final ClinicRentAgreementRepository rentAgreementRepository;
    private final ClinicRepository clinicRepository;

    @Override
    protected RentMonthlyReportFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return RentMonthlyReportFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(RentMonthlyReport entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return rentAgreementRepository
                .getClinicIdByClinicRentAgreementId(entity.getClinicRentAgreementId())
                .flatMap(clinicRepository::getDoctorIdByClinicId)
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<RentMonthlyReportFilter> getFilterClass() {
        return RentMonthlyReportFilter.class;
    }
}
