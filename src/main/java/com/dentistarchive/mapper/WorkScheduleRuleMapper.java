package com.dentistarchive.mapper;

import com.dentistarchive.dto.WorkScheduleRuleDto;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleRuleMapper implements EntityMapper<WorkScheduleRule, WorkScheduleRuleDto>{

    public WorkScheduleRuleDto toDto(WorkScheduleRule rule) {

        WorkScheduleRuleDto dto = new WorkScheduleRuleDto();

        dto.setId(rule.getId());
        dto.setCreatedAt(rule.getCreatedAt());
        dto.setCreatedBy(rule.getCreatedBy());
        dto.setUpdatedAt(rule.getUpdatedAt());
        dto.setUpdatedBy(rule.getUpdatedBy());
        dto.setArchivedAt(rule.getArchivedAt());
        dto.setArchivedBy(rule.getArchivedBy());
        dto.setArchived(rule.isArchived());
        dto.setDayOfWeek(rule.getDayOfWeek());
        dto.setNotes(rule.getNotes());
        dto.setStartTime(rule.getStartTime());
        dto.setEndTime(rule.getEndTime());
        dto.setScheduleId(rule.getScheduleId());

        return dto;
    }
}
