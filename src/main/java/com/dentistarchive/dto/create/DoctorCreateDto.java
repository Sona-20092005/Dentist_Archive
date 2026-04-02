package com.dentistarchive.dto.create;

//import com.skapps.tech.common.dto.create.BaseCreateDto;
//import com.skapps.tech.common.validation.NormalizedSpace;
//import com.skapps.tech.common.validation.PhoneNumber;
//import com.sksoldev.rep.actor.dto.UserDto;
import com.dentistarchive.dto.DoctorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.Locale;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class DoctorCreateDto extends BaseCreateDto<DoctorDto> {

    @Schema(description = "Full name (not unique)")
    @NotBlank
    @Pattern(regexp = FULL_NAME_PATTERN, message = FULL_NAME_PATTERN_ERROR_CODE)
    @Size(min = 2, max = 80)
    String fullName;

    @NotBlank
    @ToString.Exclude
    String password;

    @Schema(description = "Email (unique inside scope), confirmed in current session")
    @Email
    @NotBlank
    @Size(min = 1, max = 255)
    String email;

    @Schema(description = "Phone (unique inside scope)")
    String phone;

    @Schema(description = "Locale", allowableValues = {"ru", "en", "uz"}, implementation = String.class)
    Locale locale;
}
