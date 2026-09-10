package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.technician.TechnicianWorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface TechnicianWorkLogJpaRepository
        extends JpaRepository<TechnicianWorkLog, UUID>, QuerydslPredicateExecutor<TechnicianWorkLog> {
    Optional<TechnicianWorkLog> findByIdAndArchivedFalse(UUID id);
}
