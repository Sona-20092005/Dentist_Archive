package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface ClinicRentAgreementJpaRepository
        extends JpaRepository<ClinicRentAgreement, UUID>, QuerydslPredicateExecutor<ClinicRentAgreement> {
      Optional<ClinicRentAgreement> findByIdAndArchivedFalse(UUID id);

//    boolean existsByNurseIdAndEffectiveFrom(UUID nurseId, YearMonth effectiveFrom);
//
//    List<ClinicRentAgreement> findByEffectiveFrom(YearMonth effectiveFrom);

}
