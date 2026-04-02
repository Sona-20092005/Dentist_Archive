package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<Role, UUID>, QuerydslPredicateExecutor<Role> {

    Optional<Role> findByCode(String code);
}
