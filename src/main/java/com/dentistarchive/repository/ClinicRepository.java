package com.dentistarchive.repository;

import com.dentistarchive.entity.Clinic;
import com.dentistarchive.repository.jpa.ClinicJpaRepository;
import com.dentistarchive.repository.search.ClinicSearchMapper;
import com.dentistarchive.search.filter.ClinicFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicRepository extends BaseReadOnlyRepository<Clinic, ClinicFilter> {

    ClinicJpaRepository jpaRepository;

    public ClinicRepository(
            ClinicSearchMapper searchMapper,
            ClinicJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Clinic> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public Clinic save(Clinic plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
