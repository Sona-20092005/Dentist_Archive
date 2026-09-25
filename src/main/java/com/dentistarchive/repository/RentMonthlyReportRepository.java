package com.dentistarchive.repository;

import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.repository.jpa.RentMonthlyReportJpaRepository;
import com.dentistarchive.repository.search.RentMonthlyReportSearchMapper;
import com.dentistarchive.search.filter.RentMonthlyReportFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentMonthlyReportRepository extends BaseReadOnlyRepository<RentMonthlyReport, RentMonthlyReportFilter> {

    RentMonthlyReportJpaRepository jpaRepository;

    public RentMonthlyReportRepository(
            RentMonthlyReportSearchMapper searchMapper,
            RentMonthlyReportJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<RentMonthlyReport> findByClinicRentAgreementIdAndYearMonth(UUID clinicRentAgreementId, YearMonth yearMonth) {
        return jpaRepository.findByClinicRentAgreementIdAndYearMonth(clinicRentAgreementId, yearMonth);
    }


    @Transactional(readOnly = true)
    public boolean existsByClinicRentAgreementIdAndYearMonth(UUID clinicRentAgreementId, YearMonth yearMonth) {
        return jpaRepository.existsByClinicRentAgreementIdAndYearMonth(clinicRentAgreementId, yearMonth);
    }

//
//    @Transactional(readOnly = true)
//    public List<TechnicianMonthlyReport> findAllByTechnicianIdOrderByYearMonthDesc(UUID technicianId) {
//        return jpaRepository.findAllByTechnicianIdOrderByYearMonthDesc(technicianId);
//    }
//
//    @Transactional(readOnly = true)
//    public List<TechnicianMonthlyReport> findAllByYearMonth(YearMonth yearMonth) {
//        return jpaRepository.findAllByYearMonth(yearMonth);
//    }



    @Transactional
    public RentMonthlyReport save(RentMonthlyReport rentMonthlyReport) {
        return jpaRepository.save(rentMonthlyReport);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
