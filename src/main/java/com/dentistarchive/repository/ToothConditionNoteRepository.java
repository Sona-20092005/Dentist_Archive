package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.ToothConditionNote;
import com.dentistarchive.repository.jpa.ToothConditionNoteJpaRepository;
import com.dentistarchive.repository.search.ToothConditionNoteSearchMapper;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothConditionNoteRepository extends BaseReadOnlyRepository<ToothConditionNote, ToothConditionNoteFilter> {

    ToothConditionNoteJpaRepository jpaRepository;

    public ToothConditionNoteRepository(
            ToothConditionNoteSearchMapper searchMapper,
            ToothConditionNoteJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional
    public ToothConditionNote save(ToothConditionNote note) {
        return jpaRepository.save(note);
    }


}

