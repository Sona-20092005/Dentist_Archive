package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.material.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface SupplierJpaRepository
        extends JpaRepository<Supplier, UUID>, QuerydslPredicateExecutor<Supplier> {
    Optional<Supplier> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from supplier
            where id = :supplierId
            """
    )
    Optional<UUID> findDoctorIdBySupplierId(@Param("supplierId") UUID supplierId);

}
