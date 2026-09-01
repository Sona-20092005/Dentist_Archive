package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;


public interface NurseCompensationTypeChangeJpaRepository
        extends JpaRepository<NurseCompensationTypeChange, UUID>, QuerydslPredicateExecutor<NurseCompensationTypeChange> {

    boolean existsByNurseIdAndEffectiveFrom(UUID nurseId, YearMonth effectiveFrom);

    List<NurseCompensationTypeChange> findByEffectiveFrom(YearMonth effectiveFrom);

}
