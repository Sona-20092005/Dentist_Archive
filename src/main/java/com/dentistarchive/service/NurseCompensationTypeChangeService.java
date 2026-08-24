package com.dentistarchive.service;

import com.dentistarchive.dto.create.NurseCompensationTypeChangeCreateDto;
import com.dentistarchive.dto.update.NurseCompensationTypeChangeUpdateDto;
import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import com.dentistarchive.repository.NurseCompensationTypeChangeRepository;
import com.dentistarchive.search.filter.NurseCompensationTypeChangeFilter;
import com.dentistarchive.service.access.NurseCompensationTypeChangeAccessValidator;
import com.dentistarchive.service.provider.NurseCompensationTypeChangeProvider;
import com.dentistarchive.validator.NurseCompensationTypeChangeValidator;
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
public class NurseCompensationTypeChangeService extends BaseReadOnlyService<NurseCompensationTypeChange, NurseCompensationTypeChangeFilter> {
    NurseService nurseService;
    NurseCompensationTypeChangeRepository nurseCompensationTypeChangeRepository;
    NurseCompensationTypeChangeAccessValidator accessValidator;
    NurseCompensationTypeChangeProvider nurseCompensationTypeChangeProvider;
    NurseCompensationTypeChangeValidator  nurseCompensationTypeChangeValidator;

    public NurseCompensationTypeChangeService(
            NurseService nurseService,
            NurseCompensationTypeChangeRepository nurseCompensationTypeChangeRepository,
            NurseCompensationTypeChangeAccessValidator accessValidator,
            NurseCompensationTypeChangeProvider nurseCompensationTypeChangeProvider,
            NurseCompensationTypeChangeValidator nurseCompensationTypeChangeValidator
    ) {
        super(
                NurseCompensationTypeChange.class,
                NurseCompensationTypeChangeFilter.class,
                nurseCompensationTypeChangeRepository,
                accessValidator
        );
        this.nurseService = nurseService;
        this.nurseCompensationTypeChangeRepository = nurseCompensationTypeChangeRepository;
        this.accessValidator = accessValidator;
        this.nurseCompensationTypeChangeProvider = nurseCompensationTypeChangeProvider;
        this.nurseCompensationTypeChangeValidator = nurseCompensationTypeChangeValidator;
    }

    @Transactional
    public NurseCompensationTypeChange create(NurseCompensationTypeChangeCreateDto createDto) {
        nurseService.getAccessibleNurse(createDto.getNurseId());

        nurseCompensationTypeChangeValidator.validateCreate(createDto);
        var note = nurseCompensationTypeChangeProvider.create(createDto);
        return nurseCompensationTypeChangeRepository.save(note);
    }

    @Transactional(propagation = Propagation.NEVER)
    public NurseCompensationTypeChange update(UUID id, @Valid NurseCompensationTypeChangeUpdateDto updateDto) {
        NurseCompensationTypeChange change = getByIdOrElseThrow(id);
        accessValidator.validateAccess(change);
        nurseCompensationTypeChangeValidator.validateUpdate(change, updateDto);
        nurseCompensationTypeChangeProvider.update(change, updateDto);
        return nurseCompensationTypeChangeRepository.save(change);
    }

    @Transactional
    public void delete(UUID id) {
        NurseCompensationTypeChange change = getByIdOrElseThrow(id);
        accessValidator.validateAccess(change);
        nurseCompensationTypeChangeRepository.delete(change);
    }
}
