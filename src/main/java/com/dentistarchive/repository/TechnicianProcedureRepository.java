package com.dentistarchive.repository;

import com.dentistarchive.entity.technician.TechnicianProcedure;
import com.dentistarchive.repository.jpa.TechnicianProcedureJpaRepository;
import com.dentistarchive.repository.search.TechnicianProcedureSearchMapper;
import com.dentistarchive.search.filter.TechnicianProcedureFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianProcedureRepository extends BaseReadOnlyRepository<TechnicianProcedure, TechnicianProcedureFilter> {

    TechnicianProcedureJpaRepository jpaRepository;

    public TechnicianProcedureRepository(
            TechnicianProcedureSearchMapper searchMapper,
            TechnicianProcedureJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TechnicianProcedure> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }


    @Transactional
    public TechnicianProcedure save(TechnicianProcedure procedure) {
        return jpaRepository.save(procedure);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
