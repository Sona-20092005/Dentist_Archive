package com.dentistarchive.validator;

import com.dentistarchive.dto.create.NurseCompensationTypeChangeCreateDto;
import com.dentistarchive.dto.update.NurseCompensationTypeChangeUpdateDto;
import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import com.dentistarchive.exception.NurseCompensationTypeChangeAlreadyExistsException;
import com.dentistarchive.repository.NurseCompensationTypeChangeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class NurseCompensationTypeChangeValidator {
    NurseCompensationTypeChangeRepository nurseCompensationTypeChangeRepository;

    public void validateCreate(NurseCompensationTypeChangeCreateDto createDto) {
        if (nurseCompensationTypeChangeRepository.existsByNurseIdAndEffectiveFrom(createDto.getNurseId(), createDto.getEffectiveFrom())) {
            throw new NurseCompensationTypeChangeAlreadyExistsException();
        }
    }
    public boolean checkCurrentMonthCompensationTypeChange(NurseCompensationTypeChangeCreateDto createDto) {
        return createDto.getEffectiveFrom().equals(YearMonth.now());
    }
    public void validateUpdate(NurseCompensationTypeChange entity, NurseCompensationTypeChangeUpdateDto updateDto) {
        if (nurseCompensationTypeChangeRepository.existsByNurseIdAndEffectiveFrom(entity.getNurseId(), updateDto.getEffectiveFrom())) {
            throw new NurseCompensationTypeChangeAlreadyExistsException();
        }
    }
}
