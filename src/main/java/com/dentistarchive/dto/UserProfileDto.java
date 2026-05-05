package com.dentistarchive.dto;

import com.dentistarchive.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class UserProfileDto extends ArchivingBaseDto {

    @Schema(description = "user name (unique)")
    String userName;

    @Schema(description = "Full name (not unique)")
    String fullName;

    @Schema(description = "Email")
    String email;

    @Schema(description = "Phone")
    String phone;

    @Schema(description = "User role")
    Role role;

    Map<String, String> additionalParams;

}
