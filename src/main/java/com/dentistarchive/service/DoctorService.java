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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorService extends BaseReadOnlyService<Doctor, DoctorFilter>
        implements ArchivableService<Doctor> {

    DoctorRepository doctorRepository;
    DoctorProvider doctorProvider;
    //    EmailSender emailSender;
//    AppProperties appProperties;
    DoctorAccessValidator doctorAccessValidator;
//    ActorMsEventPublisher actorMsEventPublisher;

//    public DoctorService(UserRepository doctorRepository,
//                         UserAccessValidator userAccessValidator,
//                         UserProvider doctorProvider,
//                         EmailSender emailSender,
//                         AppProperties appProperties,
//                         ServiceProviderAccessValidator serviceProviderAccessValidator,
//                         ActorMsEventPublisher actorMsEventPublisher) {
//        super(User.class, UserFilter.class, doctorRepository, userAccessValidator);
//        this.doctorRepository = doctorRepository;
//        this.doctorProvider = doctorProvider;
//        this.emailSender = emailSender;
//        this.appProperties = appProperties;
//        this.serviceProviderAccessValidator = serviceProviderAccessValidator;
//        this.actorMsEventPublisher = actorMsEventPublisher;
//    }

    public DoctorService(DoctorRepository doctorRepository,
                         DoctorAccessValidator doctorAccessValidator,
                         DoctorProvider doctorProvider) {
        super(Doctor.class, DoctorFilter.class, doctorRepository, doctorAccessValidator);
        this.doctorRepository = doctorRepository;
        this.doctorProvider = doctorProvider;
//        this.emailSender = emailSender;
//        this.appProperties = appProperties;
        this.doctorAccessValidator = doctorAccessValidator;
//        this.actorMsEventPublisher = actorMsEventPublisher;
    }


    @Transactional
    public Doctor create(@NotNull @Valid DoctorCreateDto createDto) {
        var doctor = doctorProvider.create(createDto);
        save(doctor);
        return doctor;
    }
//
//    @Transactional(readOnly = true)
//    public Optional<Doctor> getUserByEmailAndScopeWithoutAccessControl(@NotBlank String email, @NotNull UserScope scope) {
//        return doctorRepository.getUserByEmailAndScope(email, scope);
//    }
//
//
//    @Transactional
//    public void changeUserPassword(ChangePasswordCommand command) {
//        Doctor user = getUserByEmailAndScopeWithoutAccessControl(command.getEmail(), command.getScope())
//                .filter(it -> !it.isArchived())
//                .orElseThrow(() -> new CustomValidationException(ErrorCode.USER_NOT_FOUND));
//        boolean wasTemporaryPassword = user.isTemporaryPassword();
//        doctorProvider.changePassword(user, command);
//        doctorRepository.save(user);
//    }

    @Transactional
    public void deleteByIdWithoutAccessControl(@NotNull UUID id) {
        doctorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<UUID, String> getFullNamesByIdsWithoutAccessControl(Collection<UUID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Map.of();
        }
        return doctorRepository.getFullNamesByIds(ids);
    }

    @Override
    public Doctor save(Doctor entity) {
        return doctorRepository.save(entity);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Doctor update(UUID id, @Valid DoctorUpdateDto updateDto) {
        Doctor doctor = getByIdOrElseThrow(id);
        doctorAccessValidator.validateAccess(doctor);
        doctorProvider.update(doctor, updateDto);
        return doctorRepository.save(doctor);
    }
}
