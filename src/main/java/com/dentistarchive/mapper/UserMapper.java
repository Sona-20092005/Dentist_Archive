package com.dentistarchive.mapper;

import com.dentistarchive.dto.UserDto;
import com.dentistarchive.dto.auth.UserWithPermissionsDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper implements EntityMapper<User, UserDto> {

    UserMapper userMapper;

    @Override
    public UserDto toDto(User user) {
        return switch (user) {
            case Doctor doctors -> userMapper.toDto(doctors);
            default -> throw new IllegalStateException();
        };
    }

    public UserWithPermissionsDto toWithPermissionsDto(User user) {
        return new UserWithPermissionsDto(
                toDto(user),
                user.getPasswordSetAt(),
                user.getRoles().stream()
                        .flatMap(role -> role.getPermissionCodes().stream())
                        .collect(toSet())
        );
    }
}
