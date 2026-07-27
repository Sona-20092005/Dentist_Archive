package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.ToothConditionNote;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ToothConditionNoteAccessValidator extends BaseReadOnlyAccessValidator<ToothConditionNote, ToothConditionNoteFilter> {
    final PatientRepository patientRepository;

    @Override
    protected ToothConditionNoteFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return ToothConditionNoteFilter.builder()
                .doctorId(details.getUserId())
                .build();
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
