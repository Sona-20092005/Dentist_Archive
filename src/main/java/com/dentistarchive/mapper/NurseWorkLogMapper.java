package com.dentistarchive.mapper;

import com.dentistarchive.dto.NurseWorkLogDto;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseWorkLogMapper implements EntityMapper<NurseWorkLog, NurseWorkLogDto>{

    public NurseWorkLogDto toDto(NurseWorkLog nurseWorkLog) {

        NurseWorkLogDto dto = new NurseWorkLogDto();

        dto.setId(nurseWorkLog.getId());
        dto.setCreatedAt(nurseWorkLog.getCreatedAt());
        dto.setCreatedBy(nurseWorkLog.getCreatedBy());
        dto.setUpdatedAt(nurseWorkLog.getUpdatedAt());
        dto.setUpdatedBy(nurseWorkLog.getUpdatedBy());
        dto.setArchivedAt(nurseWorkLog.getArchivedAt());
        dto.setArchivedBy(nurseWorkLog.getArchivedBy());
        dto.setArchived(nurseWorkLog.isArchived());

        dto.setNurseId(nurseWorkLog.getNurseId());
        dto.setWorkType(nurseWorkLog.getWorkType());
        dto.setDate(nurseWorkLog.getDate());
        dto.setStartTime(nurseWorkLog.getStartTime());
        dto.setEndTime(nurseWorkLog.getEndTime());
        dto.setNotes(nurseWorkLog.getNotes());
        dto.setHourlyRate(nurseWorkLog.getHourlyRate());

        return dto;
    }
}
