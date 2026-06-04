package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.CompletedTreatmentCreateDto;
import com.dentistarchive.dto.update.CompletedTreatmentUpdateDto;
import com.dentistarchive.entity.patient.CompletedTreatment;
import org.springframework.stereotype.Component;

@Component
public class CompletedTreatmentProvider {

    public CompletedTreatment create(CompletedTreatmentCreateDto createDto) {

        CompletedTreatment treatment = new CompletedTreatment();
        treatment.setDate(createDto.getDate());
        treatment.setUnitPrice(createDto.getUnitPrice());
        treatment.setNotes(createDto.getNotes());
        treatment.setToothNumbers(createDto.getToothNumbers());
        treatment.setProcedureId(createDto.getProcedureId());
        treatment.setTreatmentPlanItemId(createDto.getTreatmentPlanItemId());
        treatment.setAppointmentId(createDto.getAppointmentId());
        treatment.setPatientId(createDto.getPatientId());

        return treatment;
    }


    public CompletedTreatment update(CompletedTreatment treatment, CompletedTreatmentUpdateDto updateDto) {

        treatment.setDate(updateDto.getDate());
        treatment.setUnitPrice(updateDto.getUnitPrice());
        treatment.setNotes(updateDto.getNotes());
        treatment.setToothNumbers(updateDto.getToothNumbers());
        treatment.setProcedureId(updateDto.getProcedureId());
        treatment.setTreatmentPlanItemId(updateDto.getTreatmentPlanItemId());
        treatment.setAppointmentId(updateDto.getAppointmentId());

        return treatment;
    }
}
