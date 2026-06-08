package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.repository.TreatmentPlanRepository;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class TreatmentPlanItemAccessValidator extends BaseReadOnlyAccessValidator<TreatmentPlanItem, TreatmentPlanItemFilter> {
    PatientRepository patientRepository;
    TreatmentPlanRepository planRepository;

    public TreatmentPlanItemAccessValidator(PatientRepository patientRepository, TreatmentPlanRepository planRepository) {
        this.patientRepository = patientRepository;
        this.planRepository = planRepository;
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
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();


        return planRepository
                .getPatientIdByPlanId(entity.getTreatmentPlanId())
                .flatMap(patientRepository::getDoctorIdByPatientId)
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<TreatmentPlanItemFilter> getFilterClass() {
        return TreatmentPlanItemFilter.class;
    }
}
