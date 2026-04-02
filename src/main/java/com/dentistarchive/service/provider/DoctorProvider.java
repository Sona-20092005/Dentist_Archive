package com.dentistarchive.service.provider;

import com.dentistarchive.dto.auth.command.ChangePasswordCommand;
import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.dto.create.AdminUserCreateDto;
import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.exception.actor.CustomValidationException;
import com.dentistarchive.exception.actor.ErrorCode;
import com.dentistarchive.security.AuthUtils;
import com.dentistarchive.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.dentistarchive.utils.PasswordValidator.assertThatPasswordIsValid;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorProvider {

    RoleService roleService;
    PasswordEncoder passwordEncoder;

    public Doctor createInAdminScope(AdminUserCreateDto createDto) {
        Doctor doctor = new Doctor();
        doctor.setNickName(createDto.getNickName());
        doctor.setFullName(createDto.getFullName());
        doctor.setEmail(createDto.getEmail());
        doctor.setPhone(createDto.getPhone());
        doctor.setScope(UserScope.ADMIN);
        setPassword(doctor, createDto.getPassword(), createDto.isTemporaryPassword());
        doctor.setLocale(createDto.getLocale());

        var roles = roleService.getByIdsOrElseThrow(createDto.getRoleIds());
        roles.forEach(role -> {
            if (!role.getScope().equals(UserScope.ADMIN)) {
                throw new CustomValidationException(AdminUserCreateDto.Fields.roleIds, ErrorCode.CAN_NOT_USE_ROLE_FROM_ANOTHER_SCOPE);
            }
            if (role.isClientRole()) {
                throw new CustomValidationException(AdminUserCreateDto.Fields.roleIds, ErrorCode.USER_CAN_NOT_HAVE_CLIENT_ROLE);
            }
        });
        doctor.setRoles(new HashSet<>(roles));

        return doctor;
    }


    public Doctor createUsualUser(DoctorCreateDto createDto, boolean emailConfirmation) {

        Doctor doctor = new Doctor();
        doctor.setFullName(createDto.getFullName());
        doctor.setEmail(createDto.getEmail());
        doctor.setPhone(createDto.getPhone());
        doctor.setScope(UserScope.DOCTOR);
        doctor.setRoles(Set.of(roleService.getByCodeOrElseThrow("doctor")));
        setPassword(doctor, createDto.getPassword(), false);
        doctor.setLocale(createDto.getLocale());
        return doctor;
    }

    public void changePassword(Doctor doctor, ChangePasswordCommand command) {
        if (command.getOldPassword() == null
                || !passwordEncoder.matches(command.getOldPassword(), doctor.getPasswordHash())) {
            throw AuthUtils.getUnauthorizedException();
        }
        if (passwordEncoder.matches(command.getNewPassword(), doctor.getPasswordHash())) {
            throw new CustomValidationException(ChangePasswordCommand.Fields.newPassword,
                    ErrorCode.USER_PASSWORD_SAME_AS_OLD);
        }
        setPassword(doctor, command.getNewPassword(), false);
    }

    private void setPassword(Doctor doctor, String password, boolean isTemporary) {
        assertThatPasswordIsValid(doctor, password);

        doctor.setPasswordHash(passwordEncoder.encode(password));
        doctor.setTemporaryPassword(isTemporary);
    }
}
