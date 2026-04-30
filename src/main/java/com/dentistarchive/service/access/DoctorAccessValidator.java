package com.dentistarchive.service.access;

import com.dentistarchive.entity.Doctor;
import com.dentistarchive.enums.Role;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class DoctorAccessValidator extends BaseReadOnlyAccessValidator<Doctor, DoctorFilter> {

    @Override
    protected DoctorFilter buildAccessControlFilter() {
        return newEmptyFilter();
    }

    @Override
    protected boolean hasAccess(Doctor entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return Role.SYSTEM_ADMIN.equals(details.getRole())
                || details.getUserId().equals(entity.getId());
    }

    @Override
    protected Class<DoctorFilter> getFilterClass() {
        return DoctorFilter.class;
    }
}