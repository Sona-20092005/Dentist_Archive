package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.NursePayroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface NursePayrollJpaRepository
        extends JpaRepository<NursePayroll, UUID>, QuerydslPredicateExecutor<NursePayroll> {
    Optional<NursePayroll> findByNurseIdAndYearAndMonth(UUID nurseId, Integer year, Month month);

    boolean existsByNurseIdAndYearAndMonth(UUID nurseId, Integer year, Month month);

    List<NursePayroll> findAllByNurseIdOrderByYearDescMonthDesc(UUID nurseId);

    List<NursePayroll> findAllByYearAndMonth(Integer year, Month month);
}
