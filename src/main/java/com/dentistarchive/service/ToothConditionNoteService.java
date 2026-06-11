package com.dentistarchive.service;

import com.dentistarchive.dto.create.ToothConditionNoteCreateDto;
import com.dentistarchive.dto.update.ToothConditionNoteUpdateDto;
import com.dentistarchive.entity.patient.ToothConditionNote;
import com.dentistarchive.repository.ToothConditionNoteRepository;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import com.dentistarchive.service.access.ToothConditionNoteAccessValidator;
import com.dentistarchive.service.provider.ToothConditionNoteProvider;
import com.dentistarchive.validator.ToothConditionNoteValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothConditionNoteService extends BaseReadOnlyService<ToothConditionNote, ToothConditionNoteFilter> {
    PatientService patientService;
    ToothConditionNoteRepository toothConditionNoteRepository;
    ToothConditionNoteAccessValidator accessValidator;
    ToothConditionNoteProvider toothConditionNoteProvider;
    ToothConditionNoteValidator toothConditionNoteValidator;

    public ToothConditionNoteService(
            PatientService patientService,
            ToothConditionNoteRepository toothConditionNoteRepository,
            ToothConditionNoteAccessValidator accessValidator,
            ToothConditionNoteProvider toothConditionNoteProvider,
            ToothConditionNoteValidator toothConditionNoteValidator
    ) {
        super(
                ToothConditionNote.class,
                ToothConditionNoteFilter.class,
                toothConditionNoteRepository,
                accessValidator
        );
        this.patientService = patientService;
        this.toothConditionNoteRepository = toothConditionNoteRepository;
        this.accessValidator = accessValidator;
        this.toothConditionNoteProvider = toothConditionNoteProvider;
        this.toothConditionNoteValidator = toothConditionNoteValidator;

    }

    @Transactional
    public ToothConditionNote create(ToothConditionNoteCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());

        toothConditionNoteValidator.validate(createDto);
        var note = toothConditionNoteProvider.create(createDto);
        return toothConditionNoteRepository.save(note);
    }

    @Transactional(propagation = Propagation.NEVER)
    public ToothConditionNote update(UUID id, @Valid ToothConditionNoteUpdateDto updateDto) {
        ToothConditionNote note = getByIdOrElseThrow(id);
        accessValidator.validateAccess(note);
        toothConditionNoteProvider.update(note, updateDto);
        return toothConditionNoteRepository.save(note);
    }
}
