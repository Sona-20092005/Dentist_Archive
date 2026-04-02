package com.dentistarchive.job;

import com.sksoldev.rep.actor.client.http.UserClient;
import com.sksoldev.rep.actor.dto.create.AdminUserCreateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultDoctorsCreationJob {

    private static final String SUPER_ADMIN_NICK = "super-admin";

    UserClient userClient;

    @Value("${app.default-users.super-admin.email}")
    String superAdminEmail;
    @Value("${app.default-users.super-admin.password}")
    String superAdminPassword;

    public void createDefaultUsers(UUID superAdminRoleId) {
        log.info("Default users loading...");
        var superAdmin = userClient.getUserByNickName(SUPER_ADMIN_NICK);
        if (superAdmin.isPresent()) {
            log.info("Super admin user is already created, skip this job");
            return;
        }
        userClient.createAdminUserWithoutAccessControl(
                AdminUserCreateDto.builder()
                        .nickName(SUPER_ADMIN_NICK)
                        .fullName("Super Admin")
                        .roleIds(Set.of(superAdminRoleId))
                        .email(superAdminEmail)
                        .password(superAdminPassword)
                        .locale(Locale.ENGLISH)
                        .build()
        );
        log.info("Default users loaded");
    }
}
