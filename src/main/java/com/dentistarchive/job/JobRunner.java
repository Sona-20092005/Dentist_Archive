package com.dentistarchive.job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobRunner {

    DefaultRolesCreationJob defaultRolesCreationJob;
    DefaultDoctorsCreationJob defaultUsersCreationJob;
    DefaultClientsCreationJob defaultClientsCreationJob;

    public void runJobs() {
        defaultRolesCreationJob.createDefaultRoles();
        defaultUsersCreationJob.createDefaultUsers(defaultRolesCreationJob.getSuperAdminRoleId());
        defaultClientsCreationJob.createInternalMsClient(defaultRolesCreationJob.getInternalMsClientRoleId());
    }
}
