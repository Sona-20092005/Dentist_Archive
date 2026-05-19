package com.dentistarchive.mapper;

import com.dentistarchive.dto.UserDto;
import com.dentistarchive.dto.UserProfileDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper implements EntityMapper<User, UserDto> {

    DoctorMapper doctorMapper;

    // TODO: 4/21/2026 change switch
    @Override
    public UserDto toDto(User user) {
        return switch (user) {
            case Doctor doctors -> doctorMapper.toDto(doctors);
            default -> throw new IllegalStateException();
        };
    }

    public UserProfileDto toProfileDto(User user) {
        var profile = UserProfileDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())

                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())

                .archived(user.isArchived())
                .archivedAt(user.getArchivedAt())
                .archivedBy(user.getArchivedBy())

                .userName(user.getUserName())
                .fullName(user.getFullName())
                .role(user.getRole())
                .additionalParams(user.getAdditionalParams());

        if (user instanceof Doctor doctor) {
            profile.email(doctor.getEmail())
                    .phone(doctor.getPhone());
        }

        return profile.build();


    }

}
