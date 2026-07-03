package com.dentistarchive.mapper;

import com.dentistarchive.dto.WorkScheduleDto;
import com.dentistarchive.entity.schedule.WorkSchedule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleMapper implements EntityMapper<WorkSchedule, WorkScheduleDto>{

    public WorkScheduleDto toDto(WorkSchedule schedule) {

        WorkScheduleDto dto = new WorkScheduleDto();

        dto.setId(schedule.getId());
        dto.setCreatedAt(schedule.getCreatedAt());
        dto.setCreatedBy(schedule.getCreatedBy());
        dto.setUpdatedAt(schedule.getUpdatedAt());
        dto.setUpdatedBy(schedule.getUpdatedBy());
        dto.setArchivedAt(schedule.getArchivedAt());
        dto.setArchivedBy(schedule.getArchivedBy());
        dto.setArchived(schedule.isArchived());
        dto.setName(schedule.getName());
        dto.setNotes(schedule.getNotes());
        dto.setEffectiveFrom(schedule.getEffectiveFrom());
        dto.setEffectiveUntil(schedule.getEffectiveUntil());
        dto.setDoctorId(schedule.getDoctorId());
        dto.setClinicId(schedule.getClinicId());

        return dto;
    }
}
