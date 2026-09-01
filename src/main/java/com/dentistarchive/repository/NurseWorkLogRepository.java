package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.enums.NurseWorkType;
import com.dentistarchive.repository.jpa.NurseWorkLogJpaRepository;
import com.dentistarchive.repository.search.NurseWorkLogSearchMapper;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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


    @Transactional(readOnly = true)
    public List<NurseWorkLog> findByNurseIdAndDateBetweenAndWorkType(UUID nurseId, LocalDate start, LocalDate end, NurseWorkType workType) {
        return jpaRepository.findByNurseIdAndDateBetweenAndWorkType(nurseId, start, end, workType);
    }

    @Transactional(readOnly = true)
    public List<NurseWorkLog> findByNurseIdAndDate(UUID nurseId, LocalDate date) {
        return jpaRepository.findByNurseIdAndDate(nurseId, date);
    }

    @Transactional(readOnly = true)
    public List<NurseWorkLog> findByNurseIdAndDate(UUID nurseId, LocalDate date, UUID ignoredWorkLogId) {
        return jpaRepository.findByNurseIdAndDate(nurseId, date, ignoredWorkLogId);
    }

    @Transactional
    public NurseWorkLog save(NurseWorkLog nurseWorkLog) {
        return jpaRepository.save(nurseWorkLog);
    }

    @Transactional
    public List<NurseWorkLog> saveAll(List<NurseWorkLog> nurseWorkLogs) {
        return jpaRepository.saveAll(nurseWorkLogs);
    }


    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
