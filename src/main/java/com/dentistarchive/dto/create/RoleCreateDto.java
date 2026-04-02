package com.dentistarchive.dto.create;


import com.dentistarchive.dto.RoleDto;
import com.dentistarchive.dto.auth.enums.UserScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateDto extends BaseCreateDto<RoleDto> {

    @NotBlank
    String code;

    @NotEmpty
    Map<@NotNull Locale, @NotBlank String> namesMap;

    @NotEmpty
    Map<@NotNull Locale, @NotBlank String> descriptionsMap;

    @NotNull
    UserScope scope;

    boolean clientRole;

    @Builder.Default
    Set<@NotBlank String> permissionCodes = new HashSet<>();
}
