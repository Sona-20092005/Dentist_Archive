package com.dentistarchive.dto.create;

import com.dentistarchive.dto.DoctorDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class DoctorCreateDto extends BaseCreateDto<DoctorDto> {

    @Schema(description = "User name (unique)")
    @NotBlank
    @Size(min = 2, max = 80)
    String userName;

    @Schema(description = "Full name (not unique)")
    @NotBlank
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

}
