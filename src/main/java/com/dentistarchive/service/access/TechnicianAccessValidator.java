package com.dentistarchive.service.access;

import com.dentistarchive.entity.technician.Technician;
import com.dentistarchive.search.filter.TechnicianFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class TechnicianAccessValidator extends BaseReadOnlyAccessValidator<Technician, TechnicianFilter> {

    @Override
    protected TechnicianFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TechnicianFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(Technician entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }

    @Override
    protected Class<TechnicianFilter> getFilterClass() {
        return TechnicianFilter.class;
    }
}
