package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.WorkScheduleModificationCreateDto;
import com.dentistarchive.dto.update.WorkScheduleModificationUpdateDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import org.springframework.stereotype.Component;


@Component
public class WorkScheduleModificationProvider {

    public WorkScheduleModification create(WorkScheduleModificationCreateDto createDto) {

        WorkScheduleModification modification = new WorkScheduleModification();
        modification.setDate(createDto.getDate());
        modification.setStartTime(createDto.getStartTime());
        modification.setEndTime(createDto.getEndTime());
        modification.setModificationType(createDto.getModificationType());
        modification.setModificationTypeSortOrder(createDto.getModificationType().getSortOrder());
        modification.setWorkSessionType(createDto.getWorkSessionType());
        modification.setWorkSessionTypeSortOrder(createDto.getWorkSessionType().getSortOrder());
        modification.setSourceDate(createDto.getSourceDate());
        modification.setSourceStartTime(createDto.getSourceStartTime());
        modification.setSourceEndTime(createDto.getSourceEndTime());
        modification.setScheduleId(createDto.getScheduleId());

        return modification;
    }


    public WorkScheduleModification update(WorkScheduleModification modification, WorkScheduleModificationUpdateDto updateDto) {

        modification.setDate(updateDto.getDate());
        modification.setStartTime(updateDto.getStartTime());
        modification.setEndTime(updateDto.getEndTime());
        modification.setWorkSessionType(updateDto.getWorkSessionType());
        modification.setWorkSessionTypeSortOrder(updateDto.getWorkSessionType().getSortOrder());
        modification.setScheduleId(updateDto.getScheduleId());

        return modification;
    }
}
