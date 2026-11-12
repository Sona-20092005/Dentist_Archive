package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.NursePayroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface NursePayrollJpaRepository
        extends JpaRepository<NursePayroll, UUID>, QuerydslPredicateExecutor<NursePayroll> {
    Optional<NursePayroll> findByNurseIdAndYearMonth(UUID nurseId, YearMonth yearMonth);

    boolean existsByNurseIdAndYearMonth(UUID nurseId, YearMonth yearMonth);

    List<NursePayroll> findAllByNurseIdOrderByYearMonthDesc(UUID nurseId);

    List<NursePayroll> findAllByYearMonth(YearMonth yearMonth);
}
