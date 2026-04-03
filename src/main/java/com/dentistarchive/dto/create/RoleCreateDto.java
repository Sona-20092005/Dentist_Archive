package com.dentistarchive.dto.create;

import com.dentistarchive.dto.RoleDto;
import com.dentistarchive.dto.auth.enums.UserScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateDto extends BaseCreateDto<RoleDto> {

    @NotBlank
    String code;

    @NotNull
    UserScope scope;

    @Builder.Default
    Set<@NotBlank String> permissionCodes = new HashSet<>();

}