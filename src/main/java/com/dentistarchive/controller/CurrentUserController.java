package com.dentistarchive.controller;

import com.dentistarchive.dto.ChangePasswordCommand;
import com.dentistarchive.dto.DoctorDto;
import com.dentistarchive.dto.UserDto;
import com.dentistarchive.dto.UserProfileDto;
import com.dentistarchive.dto.update.DoctorUpdateDto;
import com.dentistarchive.mapper.DoctorMapper;
import com.dentistarchive.mapper.UserMapper;
import com.dentistarchive.service.DoctorService;
import com.dentistarchive.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Current User")
@RequestMapping("/api/v1/current-user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentUserController extends BaseController {
    UserMapper userMapper;
    UserService userService;
    DoctorMapper doctorMapper;
    DoctorService doctorService;



    @PreAuthorize("!hasRole('SUPER_ADMIN')")
    @PatchMapping("/change-password")
    @Operation(summary = "Change user's password using old password.")
    public ResponseEntity<UserDto> changeUserPassword(@RequestBody ChangePasswordCommand command) {
        return ResponseEntity.ok(
                userMapper.toDto(userService.changeUserPassword(command))
        );
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/doctor/{id}")
    @Operation(summary = "Update doctor info")
    public DoctorDto update(@PathVariable UUID id,
                            @Valid @RequestBody DoctorUpdateDto updateDto) {
        return doctorMapper.toDto(doctorService.update(id, updateDto));
    }

    @GetMapping("/current")
    public ResponseEntity<UserProfileDto> getCurrentUser() {
        return ResponseEntity.ok(userMapper.toProfileDto(userService.getCurrentUser()));
    }

}
