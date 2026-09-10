package com.dentistarchive.repository;

import com.dentistarchive.entity.technician.TechnicianWorkLog;
import com.dentistarchive.repository.jpa.TechnicianWorkLogJpaRepository;
import com.dentistarchive.repository.search.TechnicianWorkLogSearchMapper;
import com.dentistarchive.search.filter.TechnicianWorkLogFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianWorkLogRepository extends BaseReadOnlyRepository<TechnicianWorkLog, TechnicianWorkLogFilter> {

    TechnicianWorkLogJpaRepository jpaRepository;

    public TechnicianWorkLogRepository(
            TechnicianWorkLogSearchMapper searchMapper,
            TechnicianWorkLogJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TechnicianWorkLog> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public TechnicianWorkLog save(TechnicianWorkLog plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
