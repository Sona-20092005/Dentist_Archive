package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.repository.jpa.NurseWorkLogJpaRepository;
import com.dentistarchive.repository.search.NurseWorkLogSearchMapper;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseWorkLogRepository extends BaseReadOnlyRepository<NurseWorkLog, NurseWorkLogFilter> {

    NurseWorkLogJpaRepository jpaRepository;

    public NurseWorkLogRepository(
            NurseWorkLogSearchMapper searchMapper,
            NurseWorkLogJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<NurseWorkLog> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public NurseWorkLog save(NurseWorkLog nurseWorkLog) {
        return jpaRepository.save(nurseWorkLog);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
