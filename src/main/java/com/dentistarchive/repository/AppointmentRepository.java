package com.dentistarchive.repository;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import com.dentistarchive.repository.jpa.AppointmentJpaRepository;
import com.dentistarchive.repository.search.AppointmentSearchMapper;
import com.dentistarchive.search.filter.AppointmentFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentRepository extends BaseReadOnlyRepository<Appointment, AppointmentFilter> {

    AppointmentJpaRepository jpaRepository;

    public AppointmentRepository(
            AppointmentSearchMapper searchMapper,
            AppointmentJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByDoctorAndDate(UUID doctorId, LocalDate date) {
        return jpaRepository.findByDoctorAndDate(doctorId, date);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByDoctorAndDate(UUID doctorId, LocalDate date, UUID ignoredAppointmentId){
        return jpaRepository.findByDoctorAndDate(doctorId, date, ignoredAppointmentId);
    }

    public boolean hasFutureAppointments(UUID patientId,
                                         LocalDate date,
                                         AppointmentSchedulingStatus appointmentSchedulingStatus,
                                         AppointmentStatus appointmentStatus) {
        return jpaRepository.existsByPatientIdAndDateAfterAndAppointmentSchedulingStatusAndAppointmentStatusAndArchivedFalse(patientId, date, appointmentSchedulingStatus, appointmentStatus);
    }

    public boolean existsPastAppointment(UUID scheduleId, LocalDate today, LocalTime currentTime) {
        return jpaRepository.existsPastAppointment(scheduleId, today, currentTime);
    }

    public List<Appointment> findFutureActiveAppointments(UUID scheduleId, LocalDate today, LocalTime currentTime) {
        return jpaRepository.findFutureActiveAppointments(scheduleId, today, currentTime);
    }

    @Transactional
    public Appointment save(Appointment plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

}
