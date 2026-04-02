package com.dentistarchive.dto.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolesCreateDto {

    @NotEmpty
    Set<@NotNull @Valid RoleCreateDto> roles;

    public static RolesCreateDto withRoles(Set<RoleCreateDto> roles) {
        return new RolesCreateDto(roles);
    }

    public static RolesCreateDto withRoles(RoleCreateDto... roles) {
        return new RolesCreateDto(Set.of(roles));
    }
}
