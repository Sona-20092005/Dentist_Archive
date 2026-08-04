package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface NurseJpaRepository
        extends JpaRepository<Nurse, UUID>, QuerydslPredicateExecutor<Nurse> {
    Optional<Nurse> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select clinic_id
            from nurse
            where id = :nurseId
            """
    )
    Optional<UUID> findClinicIdByNurseId(@Param("nurseId") UUID nurseId);
}
