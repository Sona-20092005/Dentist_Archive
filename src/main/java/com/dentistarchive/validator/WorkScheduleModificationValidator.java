package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.repository.WorkScheduleRepository;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.ScheduleUtils.isDateWithinPeriod;
import static com.dentistarchive.utils.ScheduleUtils.isTimeRangeValid;

@Component
public class WorkScheduleModificationValidator {
    // TODO: 7/2/2026 exceptions

    private final WorkScheduleRepository workScheduleRepository;

    public WorkScheduleModificationValidator(WorkScheduleRepository workScheduleRepository) {
        this.workScheduleRepository = workScheduleRepository;
    }

    public void validate(WorkScheduleModification modification, WorkSchedule workSchedule) {
        validateTimeRanges(modification);
        validateDates(modification, workSchedule);
        validateInputs(modification);
    }


    private void validateTimeRanges(WorkScheduleModification modification) {
        if (!isTimeRangeValid(modification.getStartTime(), modification.getEndTime())) {
            throw new IllegalStateException("Modification time ranges are not valid");
        }
        if (!isTimeRangeValid(modification.getSourceStartTime(), modification.getSourceEndTime())) {
            throw new IllegalStateException("Source time ranges are not valid");
        }
    }

    private void validateDates(WorkScheduleModification modification, WorkSchedule workSchedule) {
        if (!isDateWithinPeriod(modification.getDate(), workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            throw new IllegalStateException("Modification dates are not within a valid period");
        }
        if (!isDateWithinPeriod(modification.getSourceDate(), workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            throw new IllegalStateException("Source dates are not within a valid period");
        }
    }

    private void validateInputs(WorkScheduleModification modification) {
        switch (modification.getModificationType()) {
            case ADD -> {
                if (!hasNoSource(modification)) {
                    throw new IllegalStateException("ADD must have current date fields and cannot have source fields");
                }
            }
            case MODIFY -> {
                if (!hasSource(modification)) {
                    throw new IllegalStateException("MODIFY must have current date fields and source fields");
                }
            }
            case CANCEL -> {
                if (!hasSource(modification)) {
                    throw new IllegalStateException("CANCEL cannot have current fields and must have source fields");
                }
            }
        }
    }


    private boolean hasSource(WorkScheduleModification modification) {
        return modification.getSourceDate() != null
                && modification.getSourceStartTime() != null
                && modification.getSourceEndTime() != null;
    }

    private boolean hasNoSource(WorkScheduleModification modification) {
        return modification.getSourceDate() == null
                && modification.getSourceStartTime() == null
                && modification.getSourceEndTime() == null;
    }
//
//    private void validateNoOverlap(WorkSchedule schedule) {
//
//        var schedules = workScheduleRepository.findByClinicId(schedule.getClinicId());
//
//        for (WorkSchedule existing : schedules) {
//
//            if (schedule.getId() != null && schedule.getId().equals(existing.getId())) {
//                continue;
//            }
//
//            if (periodsOverlap(existing.getEffectiveFrom(), existing.getEffectiveUntil(),
//                    schedule.getEffectiveFrom(), schedule.getEffectiveUntil())) {
//                throw new WorkScheduleOverlapException();
//            }
//        }
//    }

}