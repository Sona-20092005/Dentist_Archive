package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.search.filter.PatientFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class PatientAccessValidator extends BaseReadOnlyAccessValidator<Patient, PatientFilter> {

    @Override
    protected PatientFilter buildAccessControlFilter() {
        return null;
    }

    @Override
    protected boolean hasAccess(Patient entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }
///
    @Override
    protected Class<PatientFilter> getFilterClass() {
        return PatientFilter.class;
    }
}
