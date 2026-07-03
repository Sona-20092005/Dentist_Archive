package com.dentistarchive.repository;

import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.repository.jpa.WorkScheduleModificationJpaRepository;
import com.dentistarchive.repository.search.WorkScheduleModificationSearchMapper;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
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
public class WorkScheduleModificationRepository extends BaseReadOnlyRepository<WorkScheduleModification, WorkScheduleModificationFilter> {

    WorkScheduleModificationJpaRepository jpaRepository;

    public WorkScheduleModificationRepository(
            WorkScheduleModificationSearchMapper searchMapper,
            WorkScheduleModificationJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<WorkScheduleModification> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleModification> findByScheduleIdAndDate(UUID scheduleId, LocalDate date) {
        return jpaRepository.findByScheduleIdAndDateAndArchivedFalse(scheduleId, date);
    }

    @Transactional
    public WorkScheduleModification save(WorkScheduleModification plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
