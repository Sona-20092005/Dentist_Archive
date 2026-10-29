package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AppointmentJpaRepository
        extends JpaRepository<Appointment, UUID>, QuerydslPredicateExecutor<Appointment> {
    Optional<Appointment> findByIdAndArchivedFalse(UUID id);

    // TODO: 7/21/2026 think about the s.archive condition
    // TODO: 10/22/2026 what is the expectation if the user archives the work schedule, should those appointments not be tested for overlap

    @Query("""
        select a
        from Appointment a
        join WorkSchedule s on s.id = a.scheduleId
        where a.archived = false
          and s.archived = false
          and s.doctorId = :doctorId
          and a.date = :date
        """)
    List<Appointment> findByDoctorAndDate(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date);

    @Query("""
        select a
        from Appointment a
        join WorkSchedule s on s.id = a.scheduleId
        where a.archived = false
          and s.archived = false
          and s.doctorId = :doctorId
          and a.date = :date
          and a.id <> :ignoredAppointmentId
        """)
    List<Appointment> findByDoctorAndDate(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date,
            @Param("ignoredAppointmentId") UUID ignoredAppointmentId);

    boolean existsByPatientIdAndDateAfterAndAppointmentSchedulingStatusAndAppointmentStatusAndArchivedFalse(UUID patientId, LocalDate date, AppointmentSchedulingStatus appointmentSchedulingStatus, AppointmentStatus appointmentStatus);

    @Query("""
        select case when count(a) > 0 then true else false end
        from Appointment a
        where a.scheduleId = :scheduleId
          and a.archived = false
          and (
              a.date < :today
              or (a.date = :today and a.startTime <= :currentTime)
          )
        """)
    boolean existsPastAppointment(UUID scheduleId, LocalDate today, LocalTime currentTime);

    @Query("""
        select a
        from Appointment a
        where a.scheduleId = :scheduleId
          and a.archived = false
          and (
              a.date > :today
              or (a.date = :today and a.startTime > :currentTime)
          )
        """)
    List<Appointment> findFutureActiveAppointments(UUID scheduleId, LocalDate today, LocalTime currentTime);
}
