package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.material.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface MaterialCategoryJpaRepository
        extends JpaRepository<MaterialCategory, UUID>, QuerydslPredicateExecutor<MaterialCategory> {
    Optional<MaterialCategory> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from material_category
            where id = :categoryId
            """
    )
    Optional<UUID> findDoctorIdByCategoryId(@Param("categoryId") UUID categoryId);
}
