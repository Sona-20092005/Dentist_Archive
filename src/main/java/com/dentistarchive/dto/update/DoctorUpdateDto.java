package com.dentistarchive.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class DoctorUpdateDto {
    @Schema(description = "User name (unique)")
    @NotBlank
    @Size(min = 2, max = 80)
    String userName;

    @Schema(description = "Full name (not unique)")
    @NotBlank
    @Size(min = 2, max = 80)
    String fullName;

    @Schema(description = "Email, confirmed in current session")
    @Email
    @NotBlank
    @Size(min = 1, max = 255)
    String email;

    @Schema(description = "Phone")
    String phone;
}
