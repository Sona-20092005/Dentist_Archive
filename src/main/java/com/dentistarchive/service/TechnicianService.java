package com.dentistarchive.service;

import com.dentistarchive.dto.create.TechnicianCreateDto;
import com.dentistarchive.dto.update.TechnicianUpdateDto;
import com.dentistarchive.entity.technician.Technician;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TechnicianRepository;
import com.dentistarchive.search.filter.TechnicianFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.TechnicianAccessValidator;
import com.dentistarchive.service.provider.TechnicianProvider;
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
public class TechnicianService extends BaseReadOnlyService<Technician, TechnicianFilter>
        implements ArchivableService<Technician, TechnicianFilter> {
    TechnicianRepository technicianRepository;
    TechnicianAccessValidator accessValidator;
    TechnicianProvider technicianProvider;
    TechnicianMonthlyReportGenerator reportGenerator;

    public TechnicianService(
            TechnicianRepository technicianRepository,
            TechnicianAccessValidator accessValidator,
            TechnicianProvider technicianProvider,
            TechnicianMonthlyReportGenerator reportGenerator
    ) {
        super(
                Technician.class,
                TechnicianFilter.class,
                technicianRepository,
                accessValidator
        );
        this.technicianRepository = technicianRepository;
        this.accessValidator = accessValidator;
        this.technicianProvider = technicianProvider;
        this.reportGenerator = reportGenerator;
    }

    @Transactional
    public Technician create(TechnicianCreateDto createDto) {

        var technician = technicianProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        technician = save(technician);

        reportGenerator.generateInitialReports(technician);

        return technician;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Technician update(UUID id, @Valid TechnicianUpdateDto updateDto) {
        Technician technician = technicianRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Technician.class, id));
        accessValidator.validateAccess(technician);
        technicianProvider.update(technician, updateDto);
        return technicianRepository.save(technician);
    }

    @Override
    public Technician save(Technician entity) {
        return technicianRepository.save(entity);
    }

    @Override
    public void afterArchive(Technician entity) {}

    @Override
    public void afterUnarchive(Technician entity) {}

    public Technician getAccessibleTechnician(UUID id) {
        if (id == null) {
            return null;
        }

        Technician technician = technicianRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Technician.class, id));

        accessValidator.validateAccess(technician);

        return technician;
    }

}
