package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.ToothConditionNoteCreateDto;
import com.dentistarchive.dto.update.ToothConditionNoteUpdateDto;
import com.dentistarchive.entity.patient.ToothConditionNote;
import org.springframework.stereotype.Component;


@Component
public class ToothConditionNoteProvider {

    public ToothConditionNote create(ToothConditionNoteCreateDto createDto) {

        ToothConditionNote note = new ToothConditionNote();
        note.setToothNumber(createDto.getToothNumber());
        note.setDescription(createDto.getDescription());
        note.setPatientId(createDto.getPatientId());

        return note;
    }


    public ToothConditionNote update(ToothConditionNote note, ToothConditionNoteUpdateDto updateDto) {

        note.setDescription(updateDto.getDescription());

        return note;
    }
}
