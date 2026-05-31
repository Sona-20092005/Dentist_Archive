package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.TreatmentPlanCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanUpdateDto;
import com.dentistarchive.entity.patient.TreatmentPlan;
import org.springframework.stereotype.Component;

@Component
public class TreatmentPlanProvider {

    public TreatmentPlan create(TreatmentPlanCreateDto createDto) {

        TreatmentPlan plan = new TreatmentPlan();
        plan.setDate(createDto.getDate());
        plan.setPlanStatus(createDto.getPlanStatus());
        plan.setDiscountPercent(createDto.getDiscountPercent());
        plan.setDiscountAmount(createDto.getDiscountAmount());
        plan.setNotes(createDto.getNotes());
        plan.setPatientId(createDto.getPatientId());

        return plan;
    }


    public TreatmentPlan update(TreatmentPlan plan, TreatmentPlanUpdateDto updateDto) {

        plan.setDate(updateDto.getDate());
        plan.setPlanStatus(updateDto.getPlanStatus());
        plan.setDiscountPercent(updateDto.getDiscountPercent());
        plan.setDiscountAmount(updateDto.getDiscountAmount());
        plan.setNotes(updateDto.getNotes());

        return plan;
    }
}
