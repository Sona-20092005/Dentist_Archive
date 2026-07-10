package com.dentistarchive.validator;

import com.dentistarchive.dto.create.ToothConditionNoteCreateDto;
import com.dentistarchive.exception.ToothNoteAlreadyExistsException;
import com.dentistarchive.repository.ToothConditionNoteRepository;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.ToothNumberValidator.assertThatToothNumberIsValid;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ToothConditionNoteValidator {
    ToothConditionNoteRepository toothConditionNoteRepository;

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
