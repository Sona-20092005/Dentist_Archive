package com.dentistarchive.dto.create;

import com.dentistarchive.dto.DoctorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

// TODO: 4/2/2026 need to decide on this logic NOW 
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class AdminUserCreateDto extends BaseCreateDto<DoctorDto> {

    @Schema(description = "Unique nickname, equals to id by default", example = "user123")
    String nickName;

    @Schema(description = "Full name (not unique)")
    @NotBlank
    @Size(min = 2, max = 80)
    String fullName;

    @Schema(description = "Ids of roles")
    @NotEmpty
    Set<@NotNull UUID> roleIds;

    @ToString.Exclude
    String password;

    @Schema(description = "Password is temporary (must be changed during the first login)", defaultValue = "false")
    boolean temporaryPassword;

    @Schema(description = "Email (unique inside scope)")
    @Email
    @NotBlank
    @Size(min = 1, max = 255)
    String email;

    @Schema(description = "Phone (unique inside scope)")
    String phone;

    @Schema(description = "Locale", allowableValues = {"ru", "en", "uz"}, implementation = String.class)
    Locale locale;
}
