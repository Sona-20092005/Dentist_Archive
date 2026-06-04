package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class TreatmentPlanItemAccessValidator extends BaseReadOnlyAccessValidator<TreatmentPlanItem, TreatmentPlanItemFilter> {
    PatientRepository patientRepository;

    public TreatmentPlanItemAccessValidator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    protected TreatmentPlanItemFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TreatmentPlanItemFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(TreatmentPlanItem entity) {
        // TODO: 6/2/2026 fix
        return true;
//        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();
//
//        return patientRepository
//                .getDoctorIdByPatientId(entity.getPatientId())
//                .map(details.getUserId()::equals)
//                .orElse(false);
   }
    @Override
    protected Class<TreatmentPlanItemFilter> getFilterClass() {
        return TreatmentPlanItemFilter.class;
    }
}
