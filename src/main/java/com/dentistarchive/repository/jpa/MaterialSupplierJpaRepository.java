package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.material.MaterialSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface MaterialSupplierJpaRepository
        extends JpaRepository<MaterialSupplier, UUID>, QuerydslPredicateExecutor<MaterialSupplier> {
    Optional<MaterialSupplier> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select supplier_id
            from material_supplier
            where id = :materialSupplierId
            """
    )
    Optional<UUID> findSupplierIdByMaterialSupplierId(@Param("materialSupplierId") UUID materialSupplierId);
}
