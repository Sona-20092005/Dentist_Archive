package com.dentistarchive.service;

import com.dentistarchive.dto.create.NurseCreateDto;
import com.dentistarchive.dto.update.NurseUpdateDto;
import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.NurseRepository;
import com.dentistarchive.search.filter.NurseFilter;
import com.dentistarchive.service.access.NurseAccessValidator;
import com.dentistarchive.service.provider.NurseProvider;
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
public class NurseService extends BaseReadOnlyService<Nurse, NurseFilter>
        implements ArchivableService<Nurse, NurseFilter> {
    NurseRepository nurseRepository;
    NurseAccessValidator accessValidator;
    NurseProvider nurseProvider;
    ClinicService clinicService;
    NursePayrollGenerator payrollGenerator;

    public NurseService(
            NurseRepository nurseRepository,
            NurseAccessValidator accessValidator,
            NurseProvider nurseProvider,
            ClinicService clinicService,
            NursePayrollGenerator payrollGenerator
    ) {
        super(
                Nurse.class,
                NurseFilter.class,
                nurseRepository,
                accessValidator
        );
        this.nurseRepository = nurseRepository;
        this.accessValidator = accessValidator;
        this.nurseProvider = nurseProvider;
        this.clinicService = clinicService;
        this.payrollGenerator = payrollGenerator;
    }

    @Transactional
    public Nurse create(NurseCreateDto createDto) {
        clinicService.getAccessibleClinic(createDto.getClinicId());

        var nurse = nurseProvider.create(createDto);
        nurse = save(nurse);

        payrollGenerator.generateInitialPayrolls(nurse);

        return nurse;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Nurse update(UUID id, @Valid NurseUpdateDto updateDto) {
        clinicService.getAccessibleClinic(updateDto.getClinicId());

        Nurse nurse = nurseRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Nurse.class, id));

        accessValidator.validateAccess(nurse);
        nurseProvider.update(nurse, updateDto);
        return nurseRepository.save(nurse);
    }

    @Override
    public Nurse save(Nurse entity) {
        return nurseRepository.save(entity);
    }

    @Override
    public void afterArchive(Nurse entity) {}

    @Override
    public void afterUnarchive(Nurse entity) {}

    public Nurse getAccessibleNurse(UUID id) {
        if (id == null) {
            return null;
        }

        Nurse nurse = nurseRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Nurse.class, id));

        accessValidator.validateAccess(nurse);

        return nurse;
    }

}
