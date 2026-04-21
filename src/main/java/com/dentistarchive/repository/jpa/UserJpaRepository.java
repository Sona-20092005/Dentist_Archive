package com.dentistarchive.repository.jpa;


import com.dentistarchive.entity.User;
import com.dentistarchive.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUserName(String userName);

    boolean existsByRole(Role role);
}
