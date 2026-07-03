package com.dentistarchive.repository;

import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.repository.jpa.WorkScheduleRuleJpaRepository;
import com.dentistarchive.repository.search.WorkScheduleRuleSearchMapper;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleRuleRepository extends BaseReadOnlyRepository<WorkScheduleRule, WorkScheduleRuleFilter> {

    WorkScheduleRuleJpaRepository jpaRepository;

    public WorkScheduleRuleRepository(
            WorkScheduleRuleSearchMapper searchMapper,
            WorkScheduleRuleJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<WorkScheduleRule> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public WorkScheduleRule save(WorkScheduleRule plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public List<WorkScheduleRule> saveAll(Collection<WorkScheduleRule> rules) {
        return jpaRepository.saveAll(rules);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleRule> findByScheduleId(UUID scheduleId) {
        return jpaRepository.findByScheduleIdAndArchivedFalse(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleRule> findByScheduleIdIn(Collection<UUID> scheduleIds) {
        return jpaRepository.findByScheduleIdInAndArchivedFalse(scheduleIds);
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleRule> findByScheduleIdAndDayOfWeek(UUID scheduleId, DayOfWeek dayOfWeek) {
        return jpaRepository.findByScheduleIdAndDayOfWeekAndArchivedFalse(scheduleId, dayOfWeek);
    }
}
