package com.dentistarchive.job;

import com.dentistarchive.dto.create.AdminUserCreateDto;
import com.dentistarchive.repository.DoctorRepository;
import com.dentistarchive.service.provider.DoctorProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultDoctorsCreationJob {

    private static final String SUPER_ADMIN_NICK = "super-admin";

    DoctorRepository doctorRepository;
    DoctorProvider doctorProvider;

    // TODO: 4/3/2026 implement it later
//    @Value("${app.default-users.super-admin.email}")
//    String superAdminEmail;
//    @Value("${app.default-users.super-admin.password}")
//    String superAdminPassword;

    public void createDefaultUsers(UUID superAdminRoleId) {
        log.info("Default users loading...");
        try {
            var superAdmin = doctorRepository.getUserByNickname(SUPER_ADMIN_NICK);
            if (superAdmin.isPresent()) {
                log.info("Super admin user is already created, skip this job");
                return;
            }

            var dto = AdminUserCreateDto.builder()
                    .nickName(SUPER_ADMIN_NICK)
                    .fullName("Super Admin")
                    .roleIds(Set.of(superAdminRoleId))
                    .email("super@test.com")
                    .password("PasPas123!!")
                    .temporaryPassword(false)
                    .build();

            var doctor = doctorProvider.createInAdminScope(dto);
            doctorRepository.save(doctor);
            log.info("Default users loaded");
        } catch (Exception e) {
            log.error("Failed to load default users", e);
            throw e;
        }
    }
}
