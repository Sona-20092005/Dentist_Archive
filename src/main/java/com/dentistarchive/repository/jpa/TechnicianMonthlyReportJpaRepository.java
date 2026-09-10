package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TechnicianMonthlyReportJpaRepository
        extends JpaRepository<TechnicianMonthlyReport, UUID>, QuerydslPredicateExecutor<TechnicianMonthlyReport> {
    Optional<TechnicianMonthlyReport> findByTechnicianIdAndYearMonth(UUID technicianId, YearMonth yearMonth);

    boolean existsByTechnicianIdAndYearMonth(UUID technicianId, YearMonth yearMonth);

    List<TechnicianMonthlyReport> findAllByTechnicianIdOrderByYearMonthDesc(UUID technicianId);

    List<TechnicianMonthlyReport> findAllByYearMonth(YearMonth yearMonth);
}
