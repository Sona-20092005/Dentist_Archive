package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface NurseJpaRepository
        extends JpaRepository<Nurse, UUID>, QuerydslPredicateExecutor<Nurse> {
    Optional<Nurse> findByIdAndArchivedFalse(UUID id);
}
