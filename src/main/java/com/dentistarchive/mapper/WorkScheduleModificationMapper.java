package com.dentistarchive.mapper;

import com.dentistarchive.dto.WorkScheduleModificationDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleModificationMapper implements EntityMapper<WorkScheduleModification, WorkScheduleModificationDto>{

    public WorkScheduleModificationDto toDto(WorkScheduleModification modification) {

        WorkScheduleModificationDto dto = new WorkScheduleModificationDto();

        dto.setId(modification.getId());
        dto.setCreatedAt(modification.getCreatedAt());
        dto.setCreatedBy(modification.getCreatedBy());
        dto.setUpdatedAt(modification.getUpdatedAt());
        dto.setUpdatedBy(modification.getUpdatedBy());
        dto.setArchivedAt(modification.getArchivedAt());
        dto.setArchivedBy(modification.getArchivedBy());
        dto.setArchived(modification.isArchived());
        dto.setDate(modification.getDate());
        dto.setStartTime(modification.getStartTime());
        dto.setEndTime(modification.getEndTime());
        dto.setModificationType(modification.getModificationType());
        dto.setWorkSessionType(modification.getWorkSessionType());
        dto.setSourceDate(modification.getSourceDate());
        dto.setSourceStartTime(modification.getSourceStartTime());
        dto.setSourceEndTime(modification.getSourceEndTime());
        dto.setScheduleId(modification.getScheduleId());

        return dto;
    }
}
