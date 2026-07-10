package com.dentistarchive.mapper;

import com.dentistarchive.dto.WorkSessionDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.enums.WorkSessionType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkSessionMapper{

    public WorkSessionDto toWorkSession(WorkScheduleRule rule, LocalDate date) {

        return WorkSessionDto.builder()
                .date(date)
                .startTime(rule.getStartTime())
                .endTime(rule.getEndTime())
                .workSessionType(WorkSessionType.REGULAR)
                .scheduleId(rule.getScheduleId())
                .build();
    }

    public WorkSessionDto toWorkSession(WorkScheduleModification modification) {

        return WorkSessionDto.builder()
                .date(modification.getDate())
                .startTime(modification.getStartTime())
                .endTime(modification.getEndTime())
                .workSessionType(modification.getWorkSessionType())
                .scheduleId(modification.getScheduleId())
                .modificationId(modification.getId())
                .build();
    }
}
