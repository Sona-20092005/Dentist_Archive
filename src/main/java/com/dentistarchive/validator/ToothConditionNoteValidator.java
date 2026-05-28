package com.dentistarchive.validator;

import com.dentistarchive.dto.create.ToothConditionNoteCreateDto;
import com.dentistarchive.exception.ToothNoteAlreadyExistsException;
import com.dentistarchive.repository.ToothConditionNoteRepository;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.ToothNumberValidator.assertThatToothNumberIsValid;

@Component
public class ToothConditionNoteValidator {
    private final ToothConditionNoteRepository toothConditionNoteRepository;

    public ToothConditionNoteValidator(ToothConditionNoteRepository toothConditionNoteRepository) {
        this.toothConditionNoteRepository = toothConditionNoteRepository;
    }

    public void validate(ToothConditionNoteCreateDto createDto) {
        assertThatToothNumberIsValid(createDto.getToothNumber());

        boolean exists = toothConditionNoteRepository.exists(
                ToothConditionNoteFilter.builder().
                        patientId(createDto.getPatientId()).
                        toothNumber(createDto.getToothNumber()).
                        build()
        );

        if (exists) {
            throw new ToothNoteAlreadyExistsException();
        }
    }
}
