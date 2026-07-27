package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.CompletedTreatment;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CompletedTreatmentAccessValidator extends BaseReadOnlyAccessValidator<CompletedTreatment, CompletedTreatmentFilter> {
    final PatientRepository patientRepository;

    @Override
    protected CompletedTreatmentFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return CompletedTreatmentFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(CompletedTreatment entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return patientRepository
                .getDoctorIdByPatientId(entity.getPatientId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<CompletedTreatmentFilter> getFilterClass() {
        return CompletedTreatmentFilter.class;
    }
}
