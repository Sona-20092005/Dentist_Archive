package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.repository.jpa.TreatmentPlanItemJpaRepository;
import com.dentistarchive.repository.search.TreatmentPlanItemSearchMapper;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanItemRepository extends BaseReadOnlyRepository<TreatmentPlanItem, TreatmentPlanItemFilter> {

    TreatmentPlanItemJpaRepository jpaRepository;

    public TreatmentPlanItemRepository(
            TreatmentPlanItemSearchMapper searchMapper,
            TreatmentPlanItemJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TreatmentPlanItem> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public TreatmentPlanItem save(TreatmentPlanItem plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
