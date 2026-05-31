package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.TreatmentPlan;
import com.dentistarchive.repository.jpa.TreatmentPlanJpaRepository;
import com.dentistarchive.repository.search.TreatmentPlanSearchMapper;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanRepository extends BaseReadOnlyRepository<TreatmentPlan, TreatmentPlanFilter> {

    TreatmentPlanJpaRepository jpaRepository;

    public TreatmentPlanRepository(
            TreatmentPlanSearchMapper searchMapper,
            TreatmentPlanJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TreatmentPlan> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public TreatmentPlan save(TreatmentPlan plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
