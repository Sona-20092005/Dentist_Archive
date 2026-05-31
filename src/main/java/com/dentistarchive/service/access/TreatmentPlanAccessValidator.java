package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.TreatmentPlan;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class TreatmentPlanAccessValidator extends BaseReadOnlyAccessValidator<TreatmentPlan, TreatmentPlanFilter> {
    PatientRepository patientRepository;

        public TreatmentPlanAccessValidator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    protected TreatmentPlanFilter buildAccessControlFilter() {
        return null;
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
