package com.dentistarchive.mapper;

import com.dentistarchive.dto.ToothConditionNoteDto;
import com.dentistarchive.entity.patient.ToothConditionNote;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothConditionNoteMapper implements EntityMapper<ToothConditionNote, ToothConditionNoteDto>{

    public ToothConditionNoteDto toDto(ToothConditionNote note) {

        ToothConditionNoteDto dto = new ToothConditionNoteDto();

        dto.setId(note.getId());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setCreatedBy(note.getCreatedBy());
        dto.setUpdatedAt(note.getUpdatedAt());
        dto.setUpdatedBy(note.getUpdatedBy());
        dto.setToothNumber(note.getToothNumber());
        dto.setDescription(note.getDescription());
        dto.setPatientId(note.getPatientId());

        return dto;
    }
}
