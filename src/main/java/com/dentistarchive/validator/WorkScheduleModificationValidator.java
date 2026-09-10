package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.exception.InvalidWorkScheduleModificationInputException;
import com.dentistarchive.exception.InvalidWorkScheduleModificationSourceException;
import com.dentistarchive.exception.WorkScheduleModificationScheduleChangeNotAllowedException;
import com.dentistarchive.exception.WorkScheduleModificationTargetOverlapException;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import com.dentistarchive.service.WorkCalendarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.dentistarchive.utils.ScheduleUtils.isDateWithinPeriod;
import static com.dentistarchive.utils.ScheduleUtils.isTimeRangeValid;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class WorkScheduleModificationValidator {
    // TODO: 7/2/2026 exceptions

    WorkCalendarService  workCalendarService;


    public void validate(WorkScheduleModification modification, WorkSchedule workSchedule) {
        validateInputs(modification);

        validateTimeRanges(modification);
        validateDates(modification, workSchedule);

        validateSource(modification, workSchedule);
        validateTarget(modification);
    }

    public void validateUnarchive(WorkScheduleModification modification, WorkSchedule workSchedule) {
        validateSource(modification, workSchedule);
        validateTarget(modification);
    }

    public void validateUpdatable(WorkScheduleModification modification) {
        if (modification.getModificationType() == ModificationType.CANCEL) {
            throw new IllegalStateException("CANCEL modifications cannot be updated");
        }
    }

    public void validateUpdate(WorkScheduleModification modification, UUID previousScheduleId, WorkSchedule workSchedule) {
        validateUpdateInputs(modification, previousScheduleId);

        validateUpdateTimeRange(modification);
        validateUpdateDates(modification, workSchedule);

        validateUpdateTarget(modification);

    }

    private void validateTimeRanges(WorkScheduleModification modification) {
        switch (modification.getModificationType()) {
            case ADD -> isTargetTimeRangeValid(modification);
            case MODIFY -> {
                isSourceTimeRangeValid(modification);
                isTargetTimeRangeValid(modification);
            }
            case CANCEL -> isSourceTimeRangeValid(modification);
        }
    }

    private void validateUpdateTimeRange(WorkScheduleModification modification) {
        isTargetTimeRangeValid(modification);
    }

    private void isTargetTimeRangeValid(WorkScheduleModification modification) {
        if (!isTimeRangeValid(modification.getStartTime(), modification.getEndTime())) {
            throw new IllegalStateException("Target time ranges are not valid");
        }
    }

    private void isSourceTimeRangeValid(WorkScheduleModification modification) {
        if (!isTimeRangeValid(modification.getSourceStartTime(), modification.getSourceEndTime())) {
            throw new IllegalStateException("Source time ranges are not valid");
        }
    }

    private void validateDates(WorkScheduleModification modification, WorkSchedule workSchedule) {
        switch (modification.getModificationType()) {
            case ADD -> isTargetDateValid(modification, workSchedule);
            case MODIFY -> {
                isSourceDateValid(modification, workSchedule);
                isTargetDateValid(modification, workSchedule);
            }
            case CANCEL -> isSourceDateValid(modification, workSchedule);
        }
    }

    private void validateUpdateDates(WorkScheduleModification modification, WorkSchedule workSchedule) {
        isTargetDateValid(modification, workSchedule);
    }

    private void isTargetDateValid(WorkScheduleModification modification, WorkSchedule workSchedule) {
        if (!isDateWithinPeriod(modification.getDate(), workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            throw new IllegalStateException("Target date is not within a valid period");
        }
    }

    private void isSourceDateValid(WorkScheduleModification modification, WorkSchedule workSchedule) {
        if (!isDateWithinPeriod(modification.getSourceDate(), workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            throw new IllegalStateException("Source date is not within a valid period");
        }
    }

    private void validateUpdateInputs(WorkScheduleModification modification, UUID previousScheduleId) {
        hasTarget(modification);

        if((modification.getModificationType() == ModificationType.MODIFY) && !previousScheduleId.equals(modification.getScheduleId())) {
            throw new WorkScheduleModificationScheduleChangeNotAllowedException();
        }
    }


    private void validateInputs(WorkScheduleModification modification) {
        switch (modification.getModificationType()) {
            case ADD -> {
                if (!hasNoSource(modification) || !hasTarget(modification)) {
                    throw new InvalidWorkScheduleModificationInputException();
                }
            }
            case MODIFY -> {
                if (!hasSource(modification) || !hasTarget(modification)) {
                    throw new InvalidWorkScheduleModificationInputException();
                }
            }
            case CANCEL -> {
                if (!hasSource(modification) || !hasNoTarget(modification)) {
                    throw new InvalidWorkScheduleModificationInputException();
                }
            }
        }
    }

    private void validateSource(WorkScheduleModification modification, WorkSchedule workSchedule) {
        switch (modification.getModificationType()) {
            case MODIFY, CANCEL -> workCalendarService.findRuleSession(modification.getScheduleId(), modification.getSourceDate(),
                        modification.getSourceStartTime(), modification.getSourceEndTime())
                        .orElseThrow(() -> new InvalidWorkScheduleModificationSourceException());
            case ADD -> {}
        }
    }

    private void validateTarget(WorkScheduleModification modification) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        switch (modification.getModificationType()) {
            case ADD -> {
                if(workCalendarService.hasOverlappingSession(details.getUserId(), modification.getDate(),
                        modification.getStartTime(), modification.getEndTime())) {
                    throw new WorkScheduleModificationTargetOverlapException();
                }
            }
            case MODIFY -> {
                if (workCalendarService.hasOverlappingSession(
                        details.getUserId(),
                        modification.getDate(),
                        modification.getStartTime(),
                        modification.getEndTime(),
                        modification.getSourceDate(),
                        modification.getSourceStartTime(),
                        modification.getSourceEndTime())) {

                    throw new WorkScheduleModificationTargetOverlapException();
                }
            }
            case CANCEL -> {
            }
        }
    }

    private void validateUpdateTarget(WorkScheduleModification modification) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        if(workCalendarService.hasOverlappingSessionExceptModification(details.getUserId(), modification.getDate(),
                        modification.getStartTime(), modification.getEndTime(), modification.getId())) {
            throw new WorkScheduleModificationTargetOverlapException();
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

    private boolean hasTarget(WorkScheduleModification modification) {
        return modification.getDate() != null
                && modification.getStartTime() != null
                && modification.getEndTime() != null;
    }

    private boolean hasNoTarget(WorkScheduleModification modification) {
        return modification.getDate() == null
                && modification.getStartTime() == null
                && modification.getEndTime() == null;
    }

}

