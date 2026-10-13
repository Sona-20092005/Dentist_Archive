package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface MaterialJpaRepository
        extends JpaRepository<Material, UUID>, QuerydslPredicateExecutor<Material> {
    Optional<Material> findByIdAndArchivedFalse(UUID id);

    @Query(value = """
        select exists (
            select 1
            from material
            where category_id = :categoryId
              and archived = false
        )
        """, nativeQuery = true)
    boolean existsByCategoryIdAndArchivedFalse(@Param("categoryId") UUID categoryId);
}
