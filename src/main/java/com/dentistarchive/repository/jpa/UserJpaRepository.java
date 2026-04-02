package com.dentistarchive.repository.jpa;


import com.dentistarchive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {
}
