package com.dentistarchive.service;

import com.dentistarchive.dto.create.ClinicRentAgreementCreateDto;
import com.dentistarchive.dto.update.ClinicRentAgreementUpdateDto;
import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.ClinicRentAgreementRepository;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import com.dentistarchive.service.access.ClinicRentAgreementAccessValidator;
import com.dentistarchive.service.provider.ClinicRentAgreementProvider;
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
public class ClinicRentAgreementService extends BaseReadOnlyService<ClinicRentAgreement, ClinicRentAgreementFilter>
        implements ArchivableService<ClinicRentAgreement, ClinicRentAgreementFilter> {
    ClinicRentAgreementRepository clinicRentAgreementRepository;
    ClinicRentAgreementAccessValidator accessValidator;
    ClinicRentAgreementProvider clinicRentAgreementProvider;

    public ClinicRentAgreementService(
            ClinicRentAgreementRepository clinicRentAgreementRepository,
            ClinicRentAgreementAccessValidator accessValidator,
            ClinicRentAgreementProvider clinicRentAgreementProvider
    ) {
        super(
                ClinicRentAgreement.class,
                ClinicRentAgreementFilter.class,
                clinicRentAgreementRepository,
                accessValidator
        );
        this.clinicRentAgreementRepository = clinicRentAgreementRepository;
        this.accessValidator = accessValidator;
        this.clinicRentAgreementProvider = clinicRentAgreementProvider;
    }

    @Transactional
    public ClinicRentAgreement create(ClinicRentAgreementCreateDto createDto) {
        ClinicRentAgreement clinicRentAgreement = clinicRentAgreementProvider.create(createDto);
        return clinicRentAgreementRepository.save(clinicRentAgreement);

//        nurseCompensationTypeChangeValidator.validateCreate(createDto);
//        if(nurseCompensationTypeChangeValidator.checkCurrentMonthCompensationTypeChange(createDto)) {
//            if(!nurse.getCompensationType().equals(createDto.getCompensationType())) {
//                nursePayrollSynchronizer.applyCurrentMonthCompensationTypeChange(nurse, createDto.getCompensationType(), createDto.getEffectiveFrom());
//
//                nurse.setCompensationType(createDto.getCompensationType());
//                nurseRepository.save(nurse);
//            }
//
//            return nurseCompensationTypeChangeProvider.create(createDto);
//        }
//        else {
//            var note = nurseCompensationTypeChangeProvider.create(createDto);
//            return nurseCompensationTypeChangeRepository.save(note);
//        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ClinicRentAgreement update(UUID id, @Valid ClinicRentAgreementUpdateDto updateDto) {
        ClinicRentAgreement clinicRentAgreement = getByIdOrElseThrow(id);
        accessValidator.validateAccess(clinicRentAgreement);

//        nurseCompensationTypeChangeValidator.validateUpdate(change, updateDto);
//
//        if (updateDto.getEffectiveFrom().equals(YearMonth.now())) {
//
//            Nurse nurse = nurseService.getAccessibleNurse(change.getNurseId());
//
//            nursePayrollSynchronizer.applyCurrentMonthCompensationTypeChange(nurse, change.getCompensationType(), updateDto.getEffectiveFrom());
//
//            nurse.setCompensationType(change.getCompensationType());
//            nurseRepository.save(nurse);
//
//            nurseCompensationTypeChangeRepository.delete(change);
//
//            return null;
//        }

        clinicRentAgreementProvider.update(clinicRentAgreement, updateDto);

        return clinicRentAgreementRepository.save(clinicRentAgreement);
    }

    @Override
    public ClinicRentAgreement save(ClinicRentAgreement entity) {
        return clinicRentAgreementRepository.save(entity);
    }

    @Override
    public void afterArchive(ClinicRentAgreement entity) {}

    @Override
    public void afterUnarchive(ClinicRentAgreement entity) {}

    public ClinicRentAgreement getAccessibleClinicRentAgreement(UUID id) {
        if (id == null) {
            return null;
        }

        ClinicRentAgreement rentAgreement = clinicRentAgreementRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(ClinicRentAgreement.class, id));

        accessValidator.validateAccess(rentAgreement);

        return rentAgreement;
    }

//    @Transactional
//    public void applyChangesFor(YearMonth month) {
//        List<NurseCompensationTypeChange> changes = nurseCompensationTypeChangeRepository.findByEffectiveFrom(month);
//
//        for (NurseCompensationTypeChange change : changes) {
//            Nurse nurse = nurseService.getAccessibleNurse(change.getNurseId());
//
//            nurse.setCompensationType(change.getCompensationType());
//            nurseRepository.save(nurse);
//        }
//    }
}
