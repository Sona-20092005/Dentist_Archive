package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.TreatmentPlan;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TreatmentPlanAccessValidator extends BaseReadOnlyAccessValidator<TreatmentPlan, TreatmentPlanFilter> {
    final PatientRepository patientRepository;

    @Override
    protected TreatmentPlanFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TreatmentPlanFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(TreatmentPlan entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return patientRepository
                .getDoctorIdByPatientId(entity.getPatientId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<TreatmentPlanFilter> getFilterClass() {
        return TreatmentPlanFilter.class;
    }
}
