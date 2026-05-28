package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface PatientJpaRepository
        extends JpaRepository<Patient, UUID>, QuerydslPredicateExecutor<Patient> {
    Optional<Patient> findByIdAndArchivedFalse(UUID id);

    long countByDoctorId(UUID doctorId);

    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from patient
            where id = :patientId
            """
    )
    Optional<UUID> findDoctorIdByPatientId(@Param("patientId") UUID patientId);
}
