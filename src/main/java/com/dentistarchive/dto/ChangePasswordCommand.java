package com.dentistarchive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ChangePasswordCommand {

    @Schema(description = "Id of user (required if email or scope are null)")
    UUID userId;

    @NotBlank
    String newPassword;

    @Schema(description = "Change password old password (required if usingOtp is false)")
    String oldPassword;

}
