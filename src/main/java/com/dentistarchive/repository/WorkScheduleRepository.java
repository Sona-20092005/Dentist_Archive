package com.dentistarchive.repository;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.repository.jpa.WorkScheduleJpaRepository;
import com.dentistarchive.repository.search.WorkScheduleSearchMapper;
import com.dentistarchive.search.filter.WorkScheduleFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleRepository extends BaseReadOnlyRepository<WorkSchedule, WorkScheduleFilter> {

    WorkScheduleJpaRepository jpaRepository;

    public WorkScheduleRepository(
            WorkScheduleSearchMapper searchMapper,
            WorkScheduleJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<WorkSchedule> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public WorkSchedule save(WorkSchedule plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void flush() {
        jpaRepository.flush();
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<WorkSchedule> findByClinicId(UUID clinicId) {
        return jpaRepository.findByClinicIdAndArchivedFalse(clinicId);
    }

    @Transactional(readOnly = true)
    public List<WorkSchedule> findByDoctorId(UUID doctorId) {
        return jpaRepository.findByDoctorIdAndArchivedFalse(doctorId);
    }
}
