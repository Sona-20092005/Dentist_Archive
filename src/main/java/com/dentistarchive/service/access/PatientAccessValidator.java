package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.search.filter.PatientFilter;
import org.springframework.stereotype.Component;

@Component
public class PatientAccessValidator extends BaseReadOnlyAccessValidator<Patient, PatientFilter> {

    @Override
    protected PatientFilter buildAccessControlFilter() {
        return null;
    }

    @Override
    protected boolean hasAccess(Patient entity) {
        return false;
    }

    @Override
    protected Class<PatientFilter> getFilterClass() {
        return PatientFilter.class;
    }
}
