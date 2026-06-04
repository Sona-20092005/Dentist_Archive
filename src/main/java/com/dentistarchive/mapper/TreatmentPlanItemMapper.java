package com.dentistarchive.mapper;

import com.dentistarchive.dto.TreatmentPlanItemDto;
import com.dentistarchive.entity.patient.TreatmentPlanItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanItemMapper implements EntityMapper<TreatmentPlanItem, TreatmentPlanItemDto>{

    public TreatmentPlanItemDto toDto(TreatmentPlanItem item) {

        TreatmentPlanItemDto dto = new TreatmentPlanItemDto();

        dto.setId(item.getId());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setCreatedBy(item.getCreatedBy());
        dto.setUpdatedAt(item.getUpdatedAt());
        dto.setUpdatedBy(item.getUpdatedBy());
        dto.setArchivedAt(item.getArchivedAt());
        dto.setArchivedBy(item.getArchivedBy());
        dto.setArchived(item.isArchived());
        dto.setItemStatus(item.getItemStatus());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setNotes(item.getNotes());
        dto.setToothNumbers(item.getToothNumbers());
        dto.setProcedureId(item.getProcedureId());
        dto.setTreatmentPlanId(item.getTreatmentPlanId());

        return dto;
    }
}
