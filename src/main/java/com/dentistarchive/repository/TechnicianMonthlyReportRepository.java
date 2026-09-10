package com.dentistarchive.repository;

import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import com.dentistarchive.repository.jpa.TechnicianMonthlyReportJpaRepository;
import com.dentistarchive.repository.search.TechnicianMonthlyReportSearchMapper;
import com.dentistarchive.search.filter.TechnicianMonthlyReportFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMonthlyReportRepository extends BaseReadOnlyRepository<TechnicianMonthlyReport, TechnicianMonthlyReportFilter> {

    TechnicianMonthlyReportJpaRepository jpaRepository;

    public TechnicianMonthlyReportRepository(
            TechnicianMonthlyReportSearchMapper searchMapper,
            TechnicianMonthlyReportJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TechnicianMonthlyReport> findByTechnicianIdAndYearMonth(UUID technicianId, YearMonth yearMonth) {
        return jpaRepository.findByTechnicianIdAndYearMonth(technicianId, yearMonth);
    }

    @Transactional(readOnly = true)
    public boolean existsByTechnicianIdAndYearMonth(UUID technicianId, YearMonth yearMonth) {
        return jpaRepository.existsByTechnicianIdAndYearMonth(technicianId, yearMonth);
    }

    @Transactional(readOnly = true)
    public List<TechnicianMonthlyReport> findAllByTechnicianIdOrderByYearMonthDesc(UUID technicianId) {
        return jpaRepository.findAllByTechnicianIdOrderByYearMonthDesc(technicianId);
    }

    @Transactional(readOnly = true)
    public List<TechnicianMonthlyReport> findAllByYearMonth(YearMonth yearMonth) {
        return jpaRepository.findAllByYearMonth(yearMonth);
    }


    @Transactional
    public TechnicianMonthlyReport save(TechnicianMonthlyReport technicianMonthlyReport) {
        return jpaRepository.save(technicianMonthlyReport);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
