package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ClinicRentAgreementJpaRepository
        extends JpaRepository<ClinicRentAgreement, UUID>, QuerydslPredicateExecutor<ClinicRentAgreement> {
      Optional<ClinicRentAgreement> findByIdAndArchivedFalse(UUID id);

      @Query("""
            select agreement
            from ClinicRentAgreement agreement
            where agreement.clinicId = :clinicId
              and agreement.effectiveFrom <= :month
              and (agreement.effectiveUntil is null or agreement.effectiveUntil >= :month)
              and agreement.archived = false
            """)
      Optional<ClinicRentAgreement> findEffectiveAgreement(
              @Param("clinicId") UUID clinicId,
              @Param("month") YearMonth month
      );

      @Query(nativeQuery = true,
              value = """
            select clinic_id
            from clinic_rent_agreement
            where id = :clinicRentAgreementId
            """
      )
      Optional<UUID> findClinicIdByClinicRentAgreementId(@Param("clinicRentAgreementId") UUID clinicRentAgreementId);

      @Query(nativeQuery = true,
              value = """
      select *
      from clinic_rent_agreement
      where archived = false
        and effective_from <= :reportMonth
        and (
              effective_until is null
              or effective_until >= :reportMonth
            )
      """
      )
      List<ClinicRentAgreement> findAllForReportGeneration(
              @Param("reportMonth") LocalDate reportMonth
      );

}
