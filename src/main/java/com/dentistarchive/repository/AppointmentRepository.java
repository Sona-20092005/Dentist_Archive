package com.dentistarchive.repository;

import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.repository.jpa.AppointmentJpaRepository;
import com.dentistarchive.repository.search.AppointmentSearchMapper;
import com.dentistarchive.search.filter.AppointmentFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentRepository extends BaseReadOnlyRepository<Appointment, AppointmentFilter> {

    AppointmentJpaRepository jpaRepository;

    public AppointmentRepository(
            AppointmentSearchMapper searchMapper,
            AppointmentJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public Appointment save(Appointment plan) {
        return jpaRepository.save(plan);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
