package com.dentistarchive.service;

import com.dentistarchive.dto.create.RoleCreateDto;
import com.dentistarchive.dto.create.RolesCreateDto;
import com.dentistarchive.entity.Role;
import com.dentistarchive.exception.EntityNotFoundByFieldException;
import com.dentistarchive.repository.RoleRepository;
import com.dentistarchive.search.filter.RoleFilter;
import com.dentistarchive.service.access.RoleAccessValidator;
import com.dentistarchive.service.provider.RoleProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService extends BaseReadOnlyService<Role, RoleFilter> {

    RoleRepository roleRepository;
    RoleProvider roleProvider;

    public RoleService(RoleRepository roleRepository,
                       RoleAccessValidator roleAccessValidator,
                       RoleProvider roleProvider) {
        super(Role.class, RoleFilter.class, roleRepository, roleAccessValidator);
        this.roleRepository = roleRepository;
        this.roleProvider = roleProvider;
    }

    @Transactional
    public List<Role> overwriteAllRolesWithoutAccessControl(@NotNull @Valid RolesCreateDto rolesCreateDto) {
        List<Role> roles = roleRepository.getAll();

        Set<String> actualRoleCodes = rolesCreateDto.getRoles().stream()
                .map(RoleCreateDto::getCode)
                .collect(toSet());

        List<Role> notActualRoles = roles.stream()
                .filter(role -> !actualRoleCodes.contains(role.getCode()))
                .toList();
        roleRepository.deleteAll(notActualRoles);
        roles.removeAll(notActualRoles);

        rolesCreateDto.getRoles().forEach(roleCreateDto -> roles.stream()
                .filter(role -> role.getCode().equals(roleCreateDto.getCode()))
                .findFirst()
                .ifPresentOrElse(
                        role -> roleProvider.update(role, roleCreateDto),
                        () -> roles.add(roleProvider.create(roleCreateDto))
                ));
        roleRepository.saveAll(roles);
        return roles;
    }

    @Transactional(readOnly = true)
    public Role getByCodeOrElseThrow(@NotBlank String code) {
        return roleRepository.getByCode(code)
                .orElseThrow(() -> new EntityNotFoundByFieldException(Role.class, "code", code));
    }
}
