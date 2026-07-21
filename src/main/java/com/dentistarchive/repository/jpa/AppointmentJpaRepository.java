package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AppointmentJpaRepository
        extends JpaRepository<Appointment, UUID>, QuerydslPredicateExecutor<Appointment> {
    Optional<Appointment> findByIdAndArchivedFalse(UUID id);

    // TODO: 7/21/2026 think about the s.archive condition

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
}
