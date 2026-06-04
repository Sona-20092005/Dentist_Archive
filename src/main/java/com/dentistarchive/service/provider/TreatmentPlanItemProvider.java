package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.TreatmentPlanItemCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanItemUpdateDto;
import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.enums.TreatmentPlanItemStatus;
import org.springframework.stereotype.Component;

@Component
public class TreatmentPlanItemProvider {

    public TreatmentPlanItem create(TreatmentPlanItemCreateDto createDto) {

        TreatmentPlanItem item = new TreatmentPlanItem();
        item.setItemStatus(TreatmentPlanItemStatus.ACTIVE);
        item.setItemStatusSortOrder(TreatmentPlanItemStatus.ACTIVE.getSortOrder());
        item.setUnitPrice(createDto.getUnitPrice());
        item.setNotes(createDto.getNotes());
        item.setToothNumbers(createDto.getToothNumbers());
        item.setProcedureId(createDto.getProcedureId());
        item.setTreatmentPlanId(createDto.getTreatmentPlanId());

        return item;
    }


    public TreatmentPlanItem update(TreatmentPlanItem item, TreatmentPlanItemUpdateDto updateDto) {

        item.setItemStatus(updateDto.getItemStatus());
        item.setUnitPrice(updateDto.getUnitPrice());
        item.setNotes(updateDto.getNotes());
        item.setToothNumbers(updateDto.getToothNumbers());
        item.setProcedureId(updateDto.getProcedureId());

        return item;
    }
}
