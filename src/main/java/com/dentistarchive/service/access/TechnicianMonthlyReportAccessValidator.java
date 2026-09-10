package com.dentistarchive.service.access;

import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import com.dentistarchive.repository.TechnicianRepository;
import com.dentistarchive.search.filter.TechnicianMonthlyReportFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TechnicianMonthlyReportAccessValidator extends BaseReadOnlyAccessValidator<TechnicianMonthlyReport, TechnicianMonthlyReportFilter> {
    private final TechnicianRepository technicianRepository;


    @Override
    protected TechnicianMonthlyReportFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TechnicianMonthlyReportFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(TechnicianMonthlyReport entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return technicianRepository
                .getDoctorIdByTechnicianId(entity.getTechnicianId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<TechnicianMonthlyReportFilter> getFilterClass() {
        return TechnicianMonthlyReportFilter.class;
    }
}
