package com.dentistarchive.mapper;

import com.dentistarchive.dto.TreatmentPlanDto;
import com.dentistarchive.entity.patient.TreatmentPlan;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanMapper implements EntityMapper<TreatmentPlan, TreatmentPlanDto>{

    public TreatmentPlanDto toDto(TreatmentPlan plan) {

        TreatmentPlanDto dto = new TreatmentPlanDto();

        dto.setId(plan.getId());
        dto.setCreatedAt(plan.getCreatedAt());
        dto.setCreatedBy(plan.getCreatedBy());
        dto.setUpdatedAt(plan.getUpdatedAt());
        dto.setUpdatedBy(plan.getUpdatedBy());
        dto.setArchivedAt(plan.getArchivedAt());
        dto.setArchivedBy(plan.getArchivedBy());
        dto.setArchived(plan.isArchived());
        dto.setDate(plan.getDate());
        dto.setPlanStatus(plan.getPlanStatus());
        dto.setDiscountPercent(plan.getDiscountPercent());
        dto.setDiscountAmount(plan.getDiscountAmount());
        dto.setNotes(plan.getNotes());
        dto.setPatientId(plan.getPatientId());

        return dto;
    }
}
