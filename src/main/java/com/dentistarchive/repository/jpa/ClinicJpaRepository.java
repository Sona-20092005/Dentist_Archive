package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface ClinicJpaRepository
        extends JpaRepository<Clinic, UUID>, QuerydslPredicateExecutor<Clinic> {
    Optional<Clinic> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from clinic
            where id = :clinicId
            """
    )
    Optional<UUID> findDoctorIdByClinicId(@Param("clinicId") UUID clinicId);
}
