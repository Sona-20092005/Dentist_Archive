package com.dentistarchive.mapper;

import com.dentistarchive.dto.CompletedTreatmentDto;
import com.dentistarchive.entity.patient.CompletedTreatment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompletedTreatmentMapper implements EntityMapper<CompletedTreatment, CompletedTreatmentDto>{

    public CompletedTreatmentDto toDto(CompletedTreatment treatment) {

        CompletedTreatmentDto dto = new CompletedTreatmentDto();

        dto.setId(treatment.getId());
        dto.setCreatedAt(treatment.getCreatedAt());
        dto.setCreatedBy(treatment.getCreatedBy());
        dto.setUpdatedAt(treatment.getUpdatedAt());
        dto.setUpdatedBy(treatment.getUpdatedBy());
        dto.setArchivedAt(treatment.getArchivedAt());
        dto.setArchivedBy(treatment.getArchivedBy());
        dto.setArchived(treatment.isArchived());
        dto.setDate(treatment.getDate());
        dto.setUnitPrice(treatment.getUnitPrice());
        dto.setNotes(treatment.getNotes());
        dto.setToothNumbers(treatment.getToothNumbers());
        dto.setProcedureId(treatment.getProcedureId());
        dto.setTreatmentPlanItemId(treatment.getTreatmentPlanItemId());
        dto.setAppointmentId(treatment.getAppointmentId());
        dto.setPatientId(treatment.getPatientId());

        return dto;
    }
}
