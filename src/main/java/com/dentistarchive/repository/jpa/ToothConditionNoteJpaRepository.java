package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.ToothConditionNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;


public interface ToothConditionNoteJpaRepository
        extends JpaRepository<ToothConditionNote, UUID>, QuerydslPredicateExecutor<ToothConditionNote> {
}
