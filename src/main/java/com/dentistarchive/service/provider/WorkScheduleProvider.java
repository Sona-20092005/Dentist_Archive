package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.WorkScheduleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleUpdateDto;
import com.dentistarchive.entity.schedule.WorkSchedule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;


@Component
public class WorkScheduleProvider {

    public WorkSchedule create(WorkScheduleCreateDto createDto, UUID doctorId) {

        WorkSchedule schedule = new WorkSchedule();
        schedule.setName(createDto.getName());
        schedule.setEffectiveFrom(createDto.getEffectiveFrom());
        schedule.setEffectiveUntil(createDto.getEffectiveUntil());
        schedule.setNotes(createDto.getNotes());
        schedule.setClinicId(createDto.getClinicId());
        schedule.setDoctorId(doctorId);

        return schedule;
    }


    public WorkSchedule update(WorkSchedule schedule, WorkScheduleUpdateDto updateDto) {

        schedule.setName(updateDto.getName());
        schedule.setEffectiveUntil(updateDto.getEffectiveUntil());
        schedule.setEffectiveFrom(updateDto.getEffectiveFrom());
        schedule.setClinicId(updateDto.getClinicId());
        schedule.setNotes(updateDto.getNotes());

        return schedule;
    }

    public WorkSchedule createRevision(WorkSchedule original, LocalDate revisionDate, LocalDate effectiveUntil) {
        return WorkSchedule.builder()
                .doctorId(original.getDoctorId())
                .clinicId(original.getClinicId())
                .name(original.getName())
                .notes(original.getNotes())
                .effectiveFrom(revisionDate)
                .effectiveUntil(effectiveUntil)
                .build();
    }
}
