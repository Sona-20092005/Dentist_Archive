package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.pricelist.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface ProcedureJpaRepository
        extends JpaRepository<Procedure, UUID>, QuerydslPredicateExecutor<Procedure> {
    Optional<Procedure> findByIdAndArchivedFalse(UUID id);
}
