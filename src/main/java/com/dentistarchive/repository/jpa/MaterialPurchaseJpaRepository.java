package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.material.MaterialPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface MaterialPurchaseJpaRepository
        extends JpaRepository<MaterialPurchase, UUID>, QuerydslPredicateExecutor<MaterialPurchase> {
    Optional<MaterialPurchase> findByIdAndArchivedFalse(UUID id);

}
