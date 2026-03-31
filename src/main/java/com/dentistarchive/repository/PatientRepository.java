package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.repository.jpa.PatientJpaRepository;
import com.dentistarchive.repository.search.PatientSearchMapper;
import com.dentistarchive.search.filter.PatientFilter;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientRepository extends BaseReadOnlyRepository<Patient, PatientFilter> {

    PatientJpaRepository jpaRepository;
    EntityManager entityManager;
    JdbcClient jdbcClient;

    public PatientRepository(
            PatientSearchMapper searchMapper,
            PatientJpaRepository jpaRepository,
            EntityManager entityManager,
            JdbcClient jdbcClient
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public <T extends Patient> T save(T announcement) {
        return jpaRepository.save(announcement);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
