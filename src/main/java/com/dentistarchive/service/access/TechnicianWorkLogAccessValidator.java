package com.dentistarchive.service.access;

import com.dentistarchive.entity.technician.TechnicianWorkLog;
import com.dentistarchive.repository.TechnicianRepository;
import com.dentistarchive.search.filter.TechnicianWorkLogFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TechnicianWorkLogAccessValidator extends BaseReadOnlyAccessValidator<TechnicianWorkLog, TechnicianWorkLogFilter> {
    private final TechnicianRepository technicianRepository;


    @Override
    protected TechnicianWorkLogFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TechnicianWorkLogFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(TechnicianWorkLog entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return technicianRepository
                .getDoctorIdByTechnicianId(entity.getTechnicianId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<TechnicianWorkLogFilter> getFilterClass() {
        return TechnicianWorkLogFilter.class;
    }
}
