package com.dentistarchive.repository;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.repository.jpa.ClinicRentAgreementJpaRepository;
import com.dentistarchive.repository.search.ClinicRentAgreementSearchMapper;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicRentAgreementRepository extends BaseReadOnlyRepository<ClinicRentAgreement, ClinicRentAgreementFilter> {

    ClinicRentAgreementJpaRepository jpaRepository;

    public ClinicRentAgreementRepository(
            ClinicRentAgreementSearchMapper searchMapper,
            ClinicRentAgreementJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<ClinicRentAgreement> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public Optional<UUID> getClinicIdByClinicRentAgreementId(UUID rentAgreementId) {
        return jpaRepository.findClinicIdByClinicRentAgreementId(rentAgreementId);
    }


    @Transactional(readOnly = true)
    public Optional<ClinicRentAgreement> findEffectiveAgreement(UUID nurseId, YearMonth month) {
        return jpaRepository.findEffectiveAgreement(nurseId, month);
    }

//    @Transactional(readOnly = true)
//    public List<ClinicRentAgreement> findByEffectiveFrom(YearMonth effectiveFrom) {
//        return jpaRepository.findByEffectiveFrom(effectiveFrom);
//    }


    @Transactional(readOnly = true)
    public List<ClinicRentAgreement> findAllForReportGeneration(LocalDate reportMonth) {
        return jpaRepository.findAllForReportGeneration(reportMonth);
    }

    @Transactional
    public ClinicRentAgreement save(ClinicRentAgreement change) {
        return jpaRepository.save(change);
    }

    @Transactional
    public void delete(ClinicRentAgreement change) {
        jpaRepository.delete(change);
    }


}

