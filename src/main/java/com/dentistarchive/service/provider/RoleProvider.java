package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.RoleCreateDto;
import com.dentistarchive.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleProvider {

    public Role create(RoleCreateDto createDto) {
        return Role.builder()
                .code(createDto.getCode())
                .namesMap(createDto.getNamesMap())
                .descriptionsMap(createDto.getDescriptionsMap())
                .clientRole(createDto.isClientRole())
                .scope(createDto.getScope())
                .permissionCodes(createDto.getPermissionCodes())
                .build();
    }

    public void update(Role role, RoleCreateDto createDto) {
        if (!role.getCode().equals(createDto.getCode())) {
            throw new IllegalArgumentException("Role's code is immutable");
        }
        if (role.isClientRole() != createDto.isClientRole()) {
            throw new IllegalArgumentException("Role's client flag is immutable");
        }
        if (!role.getScope().equals(createDto.getScope())) {
            throw new IllegalArgumentException("Role's scope is immutable");
        }
        role.setNamesMap(createDto.getNamesMap());
        role.setDescriptionsMap(createDto.getDescriptionsMap());
        role.setPermissionCodes(createDto.getPermissionCodes());
    }
}
