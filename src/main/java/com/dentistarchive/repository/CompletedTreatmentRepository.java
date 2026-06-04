package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.CompletedTreatment;
import com.dentistarchive.repository.jpa.CompletedTreatmentJpaRepository;
import com.dentistarchive.repository.search.CompletedTreatmentSearchMapper;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompletedTreatmentRepository extends BaseReadOnlyRepository<CompletedTreatment, CompletedTreatmentFilter> {

    CompletedTreatmentJpaRepository jpaRepository;

    public CompletedTreatmentRepository(
            CompletedTreatmentSearchMapper searchMapper,
            CompletedTreatmentJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CompletedTreatment> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public CompletedTreatment save(CompletedTreatment plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
