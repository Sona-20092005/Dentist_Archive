package com.dentistarchive.service.access;

import com.dentistarchive.entity.Doctor;
import com.dentistarchive.search.filter.DoctorFilter;
import org.springframework.stereotype.Component;

@Component
public class DoctorAccessValidator extends BaseReadOnlyAccessValidator<Doctor, DoctorFilter> {

    @Override
    protected DoctorFilter buildAccessControlFilter() {
        return newEmptyFilter();
    }

    @Override
    protected boolean hasAccess(Doctor entity) {
        return true;
    }

    @Override
    protected Class<DoctorFilter> getFilterClass() {
        return DoctorFilter.class;
    }
}