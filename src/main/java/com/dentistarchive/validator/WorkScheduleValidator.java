package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.exception.InvalidWorkSchedulePeriodException;
import com.dentistarchive.exception.WorkScheduleOverlapException;
import com.dentistarchive.repository.WorkScheduleRepository;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.ScheduleUtils.isPeriodValid;
import static com.dentistarchive.utils.ScheduleUtils.periodsOverlap;

@Component
public class WorkScheduleValidator {

    private final WorkScheduleRepository workScheduleRepository;

    public WorkScheduleValidator(WorkScheduleRepository workScheduleRepository) {
        this.workScheduleRepository = workScheduleRepository;
    }

    public void validate(WorkSchedule schedule) {
        validateDates(schedule);
        validateNoOverlap(schedule);
    }

    private void validateDates(WorkSchedule schedule) {
        if (!isPeriodValid(schedule.getEffectiveFrom(), schedule.getEffectiveUntil())) {
            throw new InvalidWorkSchedulePeriodException();
        }
    }

    private void validateNoOverlap(WorkSchedule schedule) {

        var schedules = workScheduleRepository.findByClinicId(schedule.getClinicId());

        for (WorkSchedule existing : schedules) {

            if (schedule.getId() != null && schedule.getId().equals(existing.getId())) {
                continue;
            }

            if (periodsOverlap(existing.getEffectiveFrom(), existing.getEffectiveUntil(),
                    schedule.getEffectiveFrom(), schedule.getEffectiveUntil())) {
                throw new WorkScheduleOverlapException();
            }
        }
    }}