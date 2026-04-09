package com.dentistarchive.dto.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponseDto {

    String accessToken;
    OffsetDateTime accessTokenIssuedAt;
    OffsetDateTime accessTokenExpiresAt;

    String refreshToken;
    OffsetDateTime refreshTokenIssuedAt;
    OffsetDateTime refreshTokenExpiresAt;
}
