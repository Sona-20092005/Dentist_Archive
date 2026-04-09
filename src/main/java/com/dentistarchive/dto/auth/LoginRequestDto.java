package com.dentistarchive.dto.auth;

import com.dentistarchive.dto.auth.enums.UserScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDto {

    @NotBlank
    String login;
    @NotBlank
    String password;
    @NotNull
    UserScope scope;
}
