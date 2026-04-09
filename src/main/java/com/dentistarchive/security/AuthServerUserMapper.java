package com.dentistarchive.security;

import com.dentistarchive.dto.UserDto;
import com.dentistarchive.dto.auth.UserWithPermissionsDto;
import org.springframework.stereotype.Component;

@Component
public class AuthServerUserMapper {

    public CustomUserDetails toUserDetails(UserWithPermissionsDto userWithPermissionsDto) {
        UserDto userDto = userWithPermissionsDto.getUser();
        return CustomUserDetails.builder()
                .userId(userDto.getId())
                .scope(userDto.getScope().name())
                .permissionCodes(userWithPermissionsDto.getPermissionCodes())
                .isUser(true)
                .build();
    }
}
