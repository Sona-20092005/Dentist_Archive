package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.ToothConditionNote;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class ToothConditionNoteAccessValidator extends BaseReadOnlyAccessValidator<ToothConditionNote, ToothConditionNoteFilter> {
    PatientRepository patientRepository;

    public ToothConditionNoteAccessValidator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    protected ToothConditionNoteFilter buildAccessControlFilter() {
        return null;
    }

    @Override
    protected boolean hasAccess(ToothConditionNote entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return patientRepository
                .getDoctorIdByPatientId(entity.getPatientId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<ToothConditionNoteFilter> getFilterClass() {
        return ToothConditionNoteFilter.class;
    }
}
