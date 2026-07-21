package com.dentistarchive.validator;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.repository.AppointmentRepository;
import com.dentistarchive.service.WorkCalendarService;
import com.dentistarchive.utils.ScheduleUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dentistarchive.utils.ScheduleUtils.isDateWithinPeriod;
import static com.dentistarchive.utils.ScheduleUtils.isTimeRangeValid;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AppointmentValidator {
    // TODO: 7/2/2026 exceptions
    WorkCalendarService  workCalendarService;
    AppointmentRepository appointmentRepository;

    public void validate(Appointment appointment, WorkSchedule workSchedule) {
        validateTimeRanges(appointment);
        validateDates(appointment, workSchedule);

        validateAvailability(appointment);
        validateNoOverlap(appointment, workSchedule);
    }

    public void validateUpdate(Appointment appointment, WorkSchedule workSchedule) {
        validateTimeRanges(appointment);
        validateDates(appointment, workSchedule);

        validateAvailability(appointment);
        validateNoOverlapUpdate(appointment, workSchedule);
    }

    public void validateUnarchive(Appointment appointment, WorkSchedule workSchedule) {
        validateAvailability(appointment);
        validateNoOverlap(appointment, workSchedule);
    }

    private void validateTimeRanges(Appointment appointment) {
        if (!isTimeRangeValid(appointment.getStartTime(), appointment.getEndTime())) {
            throw new IllegalStateException("Target time ranges are not valid");
        }
    }

    private void validateDates(Appointment appointment, WorkSchedule workSchedule) {
        if (!isDateWithinPeriod(appointment.getDate(), workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            throw new IllegalStateException("Target date is not within a valid period");
        }
    }

    private void validateAvailability(Appointment appointment) {
        if (!workCalendarService.isAvailable(appointment.getScheduleId(), appointment.getDate(), appointment.getStartTime(), appointment.getEndTime())) {
            throw new IllegalStateException(
                    "Appointment is outside the doctor's working hours");
        }
    }

    private void validateNoOverlap(Appointment appointment, WorkSchedule schedule) {
        List<Appointment> appointments = appointmentRepository.findByDoctorAndDate(schedule.getDoctorId(), appointment.getDate());

        for (Appointment existing : appointments) {
            if (ScheduleUtils.timesOverlap(existing.getStartTime(), existing.getEndTime(), appointment.getStartTime(), appointment.getEndTime())) {
                throw new IllegalStateException("Appointment overlaps another appointment");
            }
        }
    }

    private void validateNoOverlapUpdate(Appointment appointment, WorkSchedule schedule) {
        List<Appointment> appointments = appointmentRepository.findByDoctorAndDate(schedule.getDoctorId(), appointment.getDate(), appointment.getId());

        for (Appointment existing : appointments) {
            if (ScheduleUtils.timesOverlap(existing.getStartTime(), existing.getEndTime(), appointment.getStartTime(), appointment.getEndTime())) {
                throw new IllegalStateException("Appointment overlaps another appointment");
            }
        }
    }


}

