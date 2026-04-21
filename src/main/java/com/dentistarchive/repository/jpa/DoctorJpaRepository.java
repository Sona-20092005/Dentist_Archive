package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<Doctor, UUID>, QuerydslPredicateExecutor<Doctor> {


    List<IdAndFullName> findAllByIdIn(Collection<UUID> ids);

    Optional<Doctor> findByEmail(String email);

    boolean existsByEmail(String email);

    record IdAndFullName(UUID id, String fullName) {
    }
}
