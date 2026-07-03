package com.dentistarchive.service.access;

import com.dentistarchive.entity.Clinic;
import com.dentistarchive.search.filter.ClinicFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class ClinicAccessValidator extends BaseReadOnlyAccessValidator<Clinic, ClinicFilter> {

    @Override
    protected ClinicFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return ClinicFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(Clinic entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }

    @Override
    protected Class<ClinicFilter> getFilterClass() {
        return ClinicFilter.class;
    }
}
