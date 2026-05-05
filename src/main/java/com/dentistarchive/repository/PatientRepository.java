package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.repository.jpa.PatientJpaRepository;
import com.dentistarchive.repository.search.PatientSearchMapper;
import com.dentistarchive.search.filter.PatientFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientRepository extends BaseReadOnlyRepository<Patient, PatientFilter> {

    PatientJpaRepository jpaRepository;

    public PatientRepository(
            PatientSearchMapper searchMapper,
            PatientJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }


    @Transactional
    public Patient save(Patient patient) {
        return jpaRepository.save(patient);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
