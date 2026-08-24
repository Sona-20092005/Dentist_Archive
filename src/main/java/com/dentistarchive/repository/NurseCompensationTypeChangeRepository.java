package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import com.dentistarchive.repository.jpa.NurseCompensationTypeChangeJpaRepository;
import com.dentistarchive.repository.search.NurseCompensationTypeChangeSearchMapper;
import com.dentistarchive.search.filter.NurseCompensationTypeChangeFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseCompensationTypeChangeRepository extends BaseReadOnlyRepository<NurseCompensationTypeChange, NurseCompensationTypeChangeFilter> {

    NurseCompensationTypeChangeJpaRepository jpaRepository;

    public NurseCompensationTypeChangeRepository(
            NurseCompensationTypeChangeSearchMapper searchMapper,
            NurseCompensationTypeChangeJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByNurseIdAndEffectiveFrom(UUID nurseId, YearMonth effectiveFrom) {
        return jpaRepository.existsByNurseIdAndEffectiveFrom(nurseId, effectiveFrom);
    }

    @Transactional
    public NurseCompensationTypeChange save(NurseCompensationTypeChange change) {
        return jpaRepository.save(change);
    }

    @Transactional
    public void delete(NurseCompensationTypeChange change) {
        jpaRepository.delete(change);
    }


}

