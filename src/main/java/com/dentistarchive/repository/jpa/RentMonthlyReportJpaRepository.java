package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.rent.RentMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;


public interface RentMonthlyReportJpaRepository
        extends JpaRepository<RentMonthlyReport, UUID>, QuerydslPredicateExecutor<RentMonthlyReport> {
    Optional<RentMonthlyReport> findByClinicRentAgreementIdAndYearMonth(UUID clinicRentAgreement, YearMonth yearMonth);
//
    boolean existsByClinicRentAgreementIdAndYearMonth(UUID clinicRentAgreementId, YearMonth yearMonth);
//
//    List<TechnicianMonthlyReport> findAllByTechnicianIdOrderByYearMonthDesc(UUID technicianId);
//
//    List<TechnicianMonthlyReport> findAllByYearMonth(YearMonth yearMonth);
}
