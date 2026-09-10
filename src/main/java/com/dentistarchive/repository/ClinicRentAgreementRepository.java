package com.dentistarchive.repository;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.repository.jpa.ClinicRentAgreementJpaRepository;
import com.dentistarchive.repository.search.ClinicRentAgreementSearchMapper;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

//    @Transactional(readOnly = true)
//    public boolean existsByNurseIdAndEffectiveFrom(UUID nurseId, YearMonth effectiveFrom) {
//        return jpaRepository.existsByNurseIdAndEffectiveFrom(nurseId, effectiveFrom);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ClinicRentAgreement> findByEffectiveFrom(YearMonth effectiveFrom) {
//        return jpaRepository.findByEffectiveFrom(effectiveFrom);
//    }

    @Transactional
    public ClinicRentAgreement save(ClinicRentAgreement change) {
        return jpaRepository.save(change);
    }

    @Transactional
    public void delete(ClinicRentAgreement change) {
        jpaRepository.delete(change);
    }


}

