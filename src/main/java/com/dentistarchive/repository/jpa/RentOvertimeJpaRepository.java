package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.rent.RentOvertime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface RentOvertimeJpaRepository
        extends JpaRepository<RentOvertime, UUID>, QuerydslPredicateExecutor<RentOvertime> {

    Optional<RentOvertime> findByWorkScheduleModificationId(UUID id);

}
