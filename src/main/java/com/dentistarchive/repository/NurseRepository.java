package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.repository.jpa.NurseJpaRepository;
import com.dentistarchive.repository.search.NurseSearchMapper;
import com.dentistarchive.search.filter.NurseFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseRepository extends BaseReadOnlyRepository<Nurse, NurseFilter> {

    NurseJpaRepository jpaRepository;

    public NurseRepository(
            NurseSearchMapper searchMapper,
            NurseJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Nurse> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public Nurse save(Nurse nurse) {
        return jpaRepository.save(nurse);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
