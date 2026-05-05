package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface PatientJpaRepository
        extends JpaRepository<Patient, UUID>, QuerydslPredicateExecutor<Patient> {
    Optional<Patient> findByIdAndArchivedFalse(UUID id);
}
