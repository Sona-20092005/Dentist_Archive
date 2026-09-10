package com.dentistarchive.repository;

import com.dentistarchive.entity.technician.Technician;
import com.dentistarchive.repository.jpa.TechnicianJpaRepository;
import com.dentistarchive.repository.search.TechnicianSearchMapper;
import com.dentistarchive.search.filter.TechnicianFilter;
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
public class TechnicianRepository extends BaseReadOnlyRepository<Technician, TechnicianFilter> {

    TechnicianJpaRepository jpaRepository;

    public TechnicianRepository(
            TechnicianSearchMapper searchMapper,
            TechnicianJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Technician> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }


    @Transactional(readOnly = true)
    public List<Technician> findAllForReportGeneration(LocalDate monthStart, LocalDate monthEnd) {
        return jpaRepository.findAllForReportGeneration(monthStart, monthEnd);
    }


    @Transactional(readOnly = true)
    public Optional<UUID> getDoctorIdByTechnicianId(UUID technicianId) {
        return jpaRepository.findDoctorIdByTechnicianId(technicianId);
    }

    @Transactional
    public Technician save(Technician technician) {
        return jpaRepository.save(technician);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
