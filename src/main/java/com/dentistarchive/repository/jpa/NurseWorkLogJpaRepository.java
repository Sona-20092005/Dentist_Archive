package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.enums.NurseWorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface NurseWorkLogJpaRepository
        extends JpaRepository<NurseWorkLog, UUID>, QuerydslPredicateExecutor<NurseWorkLog> {
    Optional<NurseWorkLog> findByIdAndArchivedFalse(UUID id);

    List<NurseWorkLog> findByNurseIdAndDateBetweenAndWorkType(UUID nurseId, LocalDate start, LocalDate end, NurseWorkType workType);

    @Query("""
        select w
        from NurseWorkLog w
        where w.archived = false
          and w.nurseId = :nurseId
          and w.date = :date
        """)
    List<NurseWorkLog> findByNurseIdAndDate(@Param("nurseId") UUID nurseId, @Param("date") LocalDate date);

    @Query("""
        select w
        from NurseWorkLog w
        where w.archived = false
          and w.nurseId = :nurseId
          and w.date = :date
          and w.id <> :ignoredWorkLogId
        """)
    List<NurseWorkLog> findByNurseIdAndDate(@Param("nurseId") UUID nurseId, @Param("date") LocalDate date,
                                            @Param("ignoredWorkLogId") UUID ignoredWorkLogId);
}

