package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.dto.update.DoctorUpdateDto;
import com.dentistarchive.entity.Doctor;
import com.dentistarchive.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.PasswordValidator.assertThatPasswordIsValid;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorProvider {

    PasswordEncoder passwordEncoder;

    public Doctor create(DoctorCreateDto createDto) {

        Doctor doctor = new Doctor();
        doctor.setUserName(createDto.getUserName());
        doctor.setFullName(createDto.getFullName());
        doctor.setEmail(createDto.getEmail());
        doctor.setPhone(createDto.getPhone());
        doctor.setRole(Role.DOCTOR);
        setPassword(doctor, createDto.getPassword());
        return doctor;
    }

    public void update(Doctor doctor, DoctorUpdateDto updateDto) {
        doctor.setUserName(updateDto.getUserName());
        doctor.setFullName(updateDto.getFullName());
        doctor.setEmail(updateDto.getEmail());
        doctor.setPhone(updateDto.getPhone());

    }

    private void setPassword(Doctor doctor, String password) {
        assertThatPasswordIsValid(doctor, password);

        doctor.setPasswordHash(passwordEncoder.encode(password));
    }

}
