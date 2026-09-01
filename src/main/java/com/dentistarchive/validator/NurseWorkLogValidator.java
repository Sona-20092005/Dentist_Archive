package com.dentistarchive.validator;

import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.repository.NurseWorkLogRepository;
import com.dentistarchive.utils.ScheduleUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dentistarchive.utils.ScheduleUtils.isTimeRangeValid;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class NurseWorkLogValidator {
    // TODO: 7/2/2026 exceptions

    NurseWorkLogRepository nurseWorkLogRepository;

    public void validate(NurseWorkLog workLog, Nurse nurse) {
        validateInputs(workLog, nurse);
        validateTimeRanges(workLog);
        validateNoOverlap(workLog);
    }

    public void validateUpdate(NurseWorkLog workLog, Nurse nurse) {
        validateInputs(workLog, nurse);
        validateTimeRanges(workLog);
        validateNoOverlapUpdate(workLog);
    }

    public void validateUnarchive(NurseWorkLog workLog) {
        validateNoOverlap(workLog);
    }

    private void validateTimeRanges(NurseWorkLog nurseWorkLog) {
        if (!isTimeRangeValid(nurseWorkLog.getStartTime(), nurseWorkLog.getEndTime())) {
            throw new IllegalStateException("time ranges is not valid");
        }
    }

    private void validateInputs(NurseWorkLog workLog, Nurse nurse) {
        switch (workLog.getWorkType()) {
            case REGULAR -> {
                switch (nurse.getCompensationType()) {
                    case HOURLY -> {
                        if (workLog.getHourlyRate() == null) {
                            throw new IllegalArgumentException("Regular hourly rate is required for hourly nurses");
                        }
                    }
                    case SALARY -> {
                        if (workLog.getHourlyRate() != null) {
                            throw new IllegalArgumentException("Regular hourly rate should not be provided for salaried nurses");
                        }
                    }
                }
            }
            case OVERTIME -> {
                if (workLog.getHourlyRate() == null) {
                    throw new IllegalArgumentException("Overtime hourly rate can't be null");
                }
            }
        }
    }

    private void validateNoOverlap(NurseWorkLog workLog) {
        List<NurseWorkLog> workLogs = nurseWorkLogRepository.findByNurseIdAndDate(workLog.getNurseId(), workLog.getDate());

        for (NurseWorkLog existing : workLogs) {
            if (ScheduleUtils.timesOverlap(existing.getStartTime(), existing.getEndTime(), workLog.getStartTime(), workLog.getEndTime())) {
                throw new IllegalStateException("Work log overlaps another work log");
            }
        }
    }


    private void validateNoOverlapUpdate(NurseWorkLog workLog) {
        List<NurseWorkLog> workLogs = nurseWorkLogRepository.findByNurseIdAndDate(workLog.getNurseId(), workLog.getDate(), workLog.getId());

        for (NurseWorkLog existing : workLogs) {
            if (ScheduleUtils.timesOverlap(existing.getStartTime(), existing.getEndTime(), workLog.getStartTime(), workLog.getEndTime())) {
                throw new IllegalStateException("Work log overlaps another work log");
            }
        }
    }

}

