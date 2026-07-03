package com.dentistarchive.service;

import com.dentistarchive.dto.create.ClinicCreateDto;
import com.dentistarchive.dto.update.ClinicUpdateDto;
import com.dentistarchive.entity.Clinic;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.ClinicRepository;
import com.dentistarchive.search.filter.ClinicFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.ClinicAccessValidator;
import com.dentistarchive.service.provider.ClinicProvider;
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
public class ClinicService extends BaseReadOnlyService<Clinic, ClinicFilter>
        implements ArchivableService<Clinic, ClinicFilter> {
    ClinicRepository clinicRepository;
    ClinicAccessValidator accessValidator;
    ClinicProvider clinicProvider;

    public ClinicService(
            ClinicRepository clinicRepository,
            ClinicAccessValidator accessValidator,
            ClinicProvider clinicProvider
    ) {
        super(
                Clinic.class,
                ClinicFilter.class,
                clinicRepository,
                accessValidator
        );
        this.clinicRepository = clinicRepository;
        this.accessValidator = accessValidator;
        this.clinicProvider = clinicProvider;
    }

    @Transactional
    public Clinic create(ClinicCreateDto createDto) {

        var clinic = clinicProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        return save(clinic);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Clinic update(UUID id, @Valid ClinicUpdateDto updateDto) {
        Clinic clinic = clinicRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Clinic.class, id));
        accessValidator.validateAccess(clinic);
        clinicProvider.update(clinic, updateDto);
        return clinicRepository.save(clinic);
    }

    @Override
    public Clinic save(Clinic entity) {
        return clinicRepository.save(entity);
    }

    @Override
    public void afterArchive(Clinic entity) {}

    @Override
    public void afterUnarchive(Clinic entity) {}

    public Clinic getAccessibleClinic(UUID id) {
        if (id == null) {
            return null;
        }

        Clinic clinic = clinicRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Clinic.class, id));

        accessValidator.validateAccess(clinic);

        return clinic;
    }

}
