package com.dentistarchive.dto.auth;

import com.sksoldev.rep.actor.dto.enums.ActorScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginAndPasswordDto {

    @NotBlank
    String login;
    @NotBlank
    String password;
    @NotNull
    ActorScope scope;
}
