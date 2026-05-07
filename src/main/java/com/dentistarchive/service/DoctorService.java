package com.dentistarchive.service;

import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.dto.update.DoctorUpdateDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.repository.DoctorRepository;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.service.access.DoctorAccessValidator;
import com.dentistarchive.service.provider.DoctorProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class DoctorService extends BaseReadOnlyService<Doctor, DoctorFilter>
        implements ArchivableService<Doctor, DoctorFilter> {

    DoctorRepository doctorRepository;
    DoctorProvider doctorProvider;
    DoctorAccessValidator doctorAccessValidator;

    public DoctorService(DoctorRepository doctorRepository,
                         DoctorAccessValidator doctorAccessValidator,
                         DoctorProvider doctorProvider) {
        super(Doctor.class, DoctorFilter.class, doctorRepository, doctorAccessValidator);
        this.doctorRepository = doctorRepository;
        this.doctorProvider = doctorProvider;
        this.doctorAccessValidator = doctorAccessValidator;
    }


    @Transactional
    public Doctor create(@NotNull @Valid DoctorCreateDto createDto) {
        var doctor = doctorProvider.create(createDto);
        save(doctor);
        return doctor;
    }

    @Transactional
    public void deleteByIdWithoutAccessControl(@NotNull UUID id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public Doctor save(Doctor entity) {
        return doctorRepository.save(entity);
    }

    @Override
    public void afterArchive(Doctor entity) {
    }

    @Override
    public void afterUnarchive(Doctor entity) {
    }

    @Transactional(propagation = Propagation.NEVER)
    public Doctor update(UUID id, @Valid DoctorUpdateDto updateDto) {
        Doctor doctor = getByIdOrElseThrow(id);
        doctorAccessValidator.validateAccess(doctor);
        doctorProvider.update(doctor, updateDto);
        return doctorRepository.save(doctor);
    }
}
