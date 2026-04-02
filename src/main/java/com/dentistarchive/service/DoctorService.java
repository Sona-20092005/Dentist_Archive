package com.dentistarchive.service;

import com.dentistarchive.dto.auth.command.ChangePasswordCommand;
import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.dto.create.AdminUserCreateDto;
import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.exception.actor.CustomValidationException;
import com.dentistarchive.exception.actor.ErrorCode;
import com.dentistarchive.repository.DoctorRepository;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.service.access.DoctorAccessValidator;
import com.dentistarchive.service.provider.DoctorProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorService extends BaseReadOnlyService<Doctor, DoctorFilter> {

    DoctorRepository userRepository;
    DoctorProvider userProvider;
//    EmailSender emailSender;
//    AppProperties appProperties;
//    ServiceProviderAccessValidator serviceProviderAccessValidator;
//    ActorMsEventPublisher actorMsEventPublisher;

//    public DoctorService(UserRepository userRepository,
//                         UserAccessValidator userAccessValidator,
//                         UserProvider userProvider,
//                         EmailSender emailSender,
//                         AppProperties appProperties,
//                         ServiceProviderAccessValidator serviceProviderAccessValidator,
//                         ActorMsEventPublisher actorMsEventPublisher) {
//        super(User.class, UserFilter.class, userRepository, userAccessValidator);
//        this.userRepository = userRepository;
//        this.userProvider = userProvider;
//        this.emailSender = emailSender;
//        this.appProperties = appProperties;
//        this.serviceProviderAccessValidator = serviceProviderAccessValidator;
//        this.actorMsEventPublisher = actorMsEventPublisher;
//    }

    public DoctorService(DoctorRepository userRepository,
                         DoctorAccessValidator userAccessValidator,
                         DoctorProvider userProvider) {
        super(Doctor.class, DoctorFilter.class, userRepository, userAccessValidator);
        this.userRepository = userRepository;
        this.userProvider = userProvider;
//        this.emailSender = emailSender;
//        this.appProperties = appProperties;
//        this.serviceProviderAccessValidator = serviceProviderAccessValidator;
//        this.actorMsEventPublisher = actorMsEventPublisher;
    }

    @Transactional
    public Doctor createAdminUserWithoutAccessControl(@NotNull @Valid AdminUserCreateDto createDto) {
        Doctor user = userProvider.createInAdminScope(createDto);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public Doctor createUsualUser(@NotNull @Valid DoctorCreateDto createDto, boolean emailConfirmation) {
        var user = userProvider.createUsualUser(createDto, emailConfirmation);
        userRepository.save(user);
//        actorMsEventPublisher.publishUserRegisteredEvent(user);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getUserByEmailAndScopeWithoutAccessControl(@NotBlank String email, @NotNull UserScope scope) {
        return userRepository.getUserByEmailAndScope(email, scope);
    }


    @Transactional
    public void changeUserPassword(ChangePasswordCommand command) {
        Doctor user = getUserByEmailAndScopeWithoutAccessControl(command.getEmail(), command.getScope())
                .filter(it -> !it.isArchived())
                .orElseThrow(() -> new CustomValidationException(ErrorCode.USER_NOT_FOUND));
        boolean wasTemporaryPassword = user.isTemporaryPassword();
        userProvider.changePassword(user, command);
        userRepository.save(user);
    }

    @Transactional
    public void deleteByIdWithoutAccessControl(@NotNull UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<UUID, String> getFullNamesByIdsWithoutAccessControl(Collection<UUID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Map.of();
        }
        return userRepository.getFullNamesByIds(ids);
    }
}
