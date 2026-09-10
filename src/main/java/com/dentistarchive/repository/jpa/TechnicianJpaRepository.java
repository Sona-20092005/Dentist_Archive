package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.technician.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TechnicianJpaRepository
        extends JpaRepository<Technician, UUID>, QuerydslPredicateExecutor<Technician> {
    Optional<Technician> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select *
            from technician
            where archived = false
              and monthly_report_start_date <= :monthEnd
              and (
                    collaboration_end_date is null
                    or collaboration_end_date >= :monthStart
                  )
            """
    )
    List<Technician> findAllForReportGeneration(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd
    );


    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from technician
            where id = :technicianId
            """
    )
    Optional<UUID> findDoctorIdByTechnicianId(@Param("technicianId") UUID technicianId);
}
