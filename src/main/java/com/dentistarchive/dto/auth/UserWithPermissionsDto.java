package com.dentistarchive.dto.auth;

import com.dentistarchive.dto.UserDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserWithPermissionsDto {

    UserDto user;

    OffsetDateTime passwordSetAt;

    Set<String> permissionCodes;
}
