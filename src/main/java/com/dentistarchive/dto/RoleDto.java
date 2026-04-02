package com.dentistarchive.dto;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto extends BaseDto {

    @Schema(description = "Name (unique)")
    String name;

    @Schema(description = "Code (unique)")
    String code;

    @Schema(description = "Description")
    String description;

    @Schema(description = "Role can be used only for clients")
    boolean clientRole;

    @Schema(description = "Scope where role can be applied")
    UserScope scope;
}
