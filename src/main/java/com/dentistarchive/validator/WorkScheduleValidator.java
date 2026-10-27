package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.exception.InvalidWorkSchedulePeriodException;
import com.dentistarchive.exception.WorkScheduleOverlapException;
import com.dentistarchive.repository.AppointmentRepository;
import com.dentistarchive.repository.WorkScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.dentistarchive.utils.ScheduleUtils.isPeriodValid;
import static com.dentistarchive.utils.ScheduleUtils.periodsOverlap;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class WorkScheduleValidator {

    WorkScheduleRepository workScheduleRepository;
    AppointmentRepository appointmentRepository;

    public void validate(WorkSchedule schedule) {
        validateDates(schedule);
        validateNoOverlap(schedule);
    }

    public void validateArchive(WorkSchedule schedule) {
        validateNoPastAppointments(schedule);
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
    }

    public void validateNoPastAppointments(WorkSchedule schedule) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (appointmentRepository.existsPastAppointment(schedule.getId(), today, currentTime)) {
            throw new IllegalStateException("Can't archive since there are past appointments");
        }
    }

}