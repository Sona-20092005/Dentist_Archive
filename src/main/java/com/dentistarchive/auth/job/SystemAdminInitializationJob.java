package com.dentistarchive.auth.job;

import com.dentistarchive.config.properties.AppProperties;
import com.dentistarchive.entity.SystemAdminUser;
import com.dentistarchive.enums.Role;
import com.dentistarchive.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemAdminInitializationJob implements ApplicationRunner {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AppProperties appProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.SYSTEM_ADMIN)) {
            return;
        }

        SystemAdminUser systemAdmin = SystemAdminUser.builder()
                .userName(appProperties.getSystemAdmin().getUserName())
                .fullName(appProperties.getSystemAdmin().getFullName())
                .passwordHash(passwordEncoder.encode(appProperties.getSystemAdmin().getPassword()))
                .role(Role.SYSTEM_ADMIN)
                .temporaryPassword(false)
                .build();

        userRepository.save(systemAdmin);

        log.info("Base system admin user was created: {}", systemAdmin.getUserName());
    }
}