package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.WorkScheduleRuleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRuleCorrectDto;
import com.dentistarchive.dto.update.WorkScheduleRuleRevisionCreateDto;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class WorkScheduleRuleProvider {

    public WorkScheduleRule create(WorkScheduleRuleCreateDto createDto) {

        WorkScheduleRule rule = new WorkScheduleRule();
        rule.setDayOfWeek(createDto.getDayOfWeek());
        rule.setStartTime(createDto.getStartTime());
        rule.setEndTime(createDto.getEndTime());
        rule.setNotes(createDto.getNotes());
        rule.setScheduleId(createDto.getScheduleId());

        return rule;
    }

    public WorkScheduleRule create(WorkScheduleRuleRevisionCreateDto createDto, UUID scheduleId) {

        WorkScheduleRule rule = new WorkScheduleRule();
        rule.setDayOfWeek(createDto.getDayOfWeek());
        rule.setStartTime(createDto.getStartTime());
        rule.setEndTime(createDto.getEndTime());
        rule.setNotes(createDto.getNotes());
        rule.setScheduleId(scheduleId);

        return rule;
    }


    public WorkScheduleRule update(WorkScheduleRule rule, WorkScheduleRuleCorrectDto updateDto) {

        rule.setDayOfWeek(updateDto.getDayOfWeek());
        rule.setStartTime(updateDto.getStartTime());
        rule.setEndTime(updateDto.getEndTime());
        rule.setNotes(updateDto.getNotes());

        return rule;
    }
}
