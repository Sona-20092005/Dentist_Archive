package com.dentistarchive.service;

import com.dentistarchive.dto.create.NurseCompensationTypeChangeCreateDto;
import com.dentistarchive.dto.update.NurseCompensationTypeChangeUpdateDto;
import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import com.dentistarchive.repository.NurseCompensationTypeChangeRepository;
import com.dentistarchive.repository.NurseRepository;
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

import java.time.YearMonth;
import java.util.List;
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
    NursePayrollSynchronizer nursePayrollSynchronizer;
    NurseRepository nurseRepository;

    public NurseCompensationTypeChangeService(
            NurseService nurseService,
            NurseCompensationTypeChangeRepository nurseCompensationTypeChangeRepository,
            NurseCompensationTypeChangeAccessValidator accessValidator,
            NurseCompensationTypeChangeProvider nurseCompensationTypeChangeProvider,
            NurseCompensationTypeChangeValidator nurseCompensationTypeChangeValidator,
            NursePayrollSynchronizer nursePayrollSynchronizer,
            NurseRepository nurseRepository
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
        this.nursePayrollSynchronizer = nursePayrollSynchronizer;
        this.nurseRepository = nurseRepository;
    }

    // TODO: 9/1/2026 maybe separate this whole create into two controller methods
    @Transactional
    public NurseCompensationTypeChange create(NurseCompensationTypeChangeCreateDto createDto) {
        Nurse nurse = nurseService.getAccessibleNurse(createDto.getNurseId());

        nurseCompensationTypeChangeValidator.validateCreate(createDto);

        // TODO: 8/24/2026 fix this
        if(nurseCompensationTypeChangeValidator.checkCurrentMonthCompensationTypeChange(createDto)) {
            if(!nurse.getCompensationType().equals(createDto.getCompensationType())) {
                nursePayrollSynchronizer.applyCurrentMonthCompensationTypeChange(nurse, createDto.getCompensationType(), createDto.getEffectiveFrom());

                nurse.setCompensationType(createDto.getCompensationType());
                nurseRepository.save(nurse);
            }

            return nurseCompensationTypeChangeProvider.create(createDto);
        }
        else {
            var note = nurseCompensationTypeChangeProvider.create(createDto);
            return nurseCompensationTypeChangeRepository.save(note);
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public NurseCompensationTypeChange update(UUID id, @Valid NurseCompensationTypeChangeUpdateDto updateDto) {
        NurseCompensationTypeChange change = getByIdOrElseThrow(id);
        accessValidator.validateAccess(change);

        nurseCompensationTypeChangeValidator.validateUpdate(change, updateDto);

        if (updateDto.getEffectiveFrom().equals(YearMonth.now())) {

            Nurse nurse = nurseService.getAccessibleNurse(change.getNurseId());

            nursePayrollSynchronizer.applyCurrentMonthCompensationTypeChange(nurse, change.getCompensationType(), updateDto.getEffectiveFrom());

            nurse.setCompensationType(change.getCompensationType());
            nurseRepository.save(nurse);

            nurseCompensationTypeChangeRepository.delete(change);

            return null;
        }

        nurseCompensationTypeChangeProvider.update(change, updateDto);

        return nurseCompensationTypeChangeRepository.save(change);
    }

    @Transactional
    public void delete(UUID id) {
        NurseCompensationTypeChange change = getByIdOrElseThrow(id);
        accessValidator.validateAccess(change);
        nurseCompensationTypeChangeRepository.delete(change);
    }

    @Transactional
    public void applyChangesFor(YearMonth month) {
        List<NurseCompensationTypeChange> changes = nurseCompensationTypeChangeRepository.findByEffectiveFrom(month);

        for (NurseCompensationTypeChange change : changes) {
            Nurse nurse = nurseService.getAccessibleNurse(change.getNurseId());

            nurse.setCompensationType(change.getCompensationType());
            nurseRepository.save(nurse);
        }
    }
}
