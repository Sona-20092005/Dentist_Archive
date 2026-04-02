package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.Doctor;
import com.dentistarchive.dto.auth.enums.UserScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<Doctor, UUID>, QuerydslPredicateExecutor<Doctor> {

    Optional<Doctor> findByNickName(String nickName);

    Optional<Doctor> findUserByEmailAndScope(String email, UserScope scope);

    Optional<Doctor> findUserByPhoneAndScope(String phone, UserScope scope);

    List<IdAndFullName> findAllByIdIn(Collection<UUID> ids);

    record IdAndFullName(UUID id, String fullName) {}
}
