package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.NurseWorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface NurseWorkLogJpaRepository
        extends JpaRepository<NurseWorkLog, UUID>, QuerydslPredicateExecutor<NurseWorkLog> {
    Optional<NurseWorkLog> findByIdAndArchivedFalse(UUID id);
}
