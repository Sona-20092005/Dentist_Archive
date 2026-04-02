package com.dentistarchive.dto.auth.command;

import com.dentistarchive.dto.auth.enums.UserScope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ChangePasswordCommand {

    @NotBlank
    String email;

    @NotNull
    UserScope scope;

    @NotBlank
    String newPassword;

    @Schema(description = "Change password old password (required if usingOtp is false)")
    String oldPassword;

}
