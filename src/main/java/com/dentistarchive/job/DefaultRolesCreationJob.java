package com.dentistarchive.job;

import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.dto.create.RoleCreateDto;
import com.dentistarchive.dto.create.RolesCreateDto;
import com.dentistarchive.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultRolesCreationJob {

    public static final String SUPER_ADMIN_ROLE_CODE = "super-admin";
    public static final String DOCTOR_ROLE_CODE = "doctor";
    public static final String CLINIC_ROLE_CODE = "clinic";
    public static final String SYSTEM_ROLE_CODE = "system";

    RoleService roleService;

    Set<RoleCreateDto> defaultRoles = Set.of(

            RoleCreateDto.builder()
                    .code(SUPER_ADMIN_ROLE_CODE)
                    .scope(UserScope.ADMIN)
                    .permissionCodes(Set.of(
                            "read_all_users",
                            "create_user",
                            "update_user",
                            "delete_user",
                            "manage_roles",
                            "manage_clinics",
                            "manage_doctors"
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(DOCTOR_ROLE_CODE)
                    .scope(UserScope.DOCTOR)
                    .permissionCodes(Set.of(
                            "read_patients",
                            "create_appointment",
                            "update_appointment",
                            "write_medical_record"
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(CLINIC_ROLE_CODE)
                    .scope(UserScope.CLINIC)
                    .permissionCodes(Set.of(
                            "read_doctors",
                            "manage_schedule",
                            "manage_patients"
                    ))
                    .build(),

            RoleCreateDto.builder()
                    .code(SYSTEM_ROLE_CODE)
                    .scope(UserScope.SYSTEM)
                    .permissionCodes(Set.of())
                    .build()
    );

    public void createDefaultRoles() {
        log.info("Default roles loading...");
        try {
            roleService.overwriteAllRolesWithoutAccessControl(RolesCreateDto.withRoles(defaultRoles));
            log.info("Default roles loaded");
        } catch (Exception e) {
            log.error("Failed to load default roles", e);
            throw e;
        }
    }

    public java.util.UUID getSuperAdminRoleId() {
        return roleService.getByCodeOrElseThrow(SUPER_ADMIN_ROLE_CODE).getId();
    }
}