package com.dentistarchive.repository;

import com.dentistarchive.entity.pricelist.Procedure;
import com.dentistarchive.repository.jpa.ProcedureJpaRepository;
import com.dentistarchive.repository.search.ProcedureSearchMapper;
import com.dentistarchive.search.filter.ProcedureFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcedureRepository extends BaseReadOnlyRepository<Procedure, ProcedureFilter> {

    ProcedureJpaRepository jpaRepository;

    public ProcedureRepository(
            ProcedureSearchMapper searchMapper,
            ProcedureJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Procedure> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }


    @Transactional
    public Procedure save(Procedure procedure) {
        return jpaRepository.save(procedure);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
