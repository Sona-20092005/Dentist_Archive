package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.ClinicCreateDto;
import com.dentistarchive.dto.update.ClinicUpdateDto;
import com.dentistarchive.entity.Clinic;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class ClinicProvider {

    public Clinic create(ClinicCreateDto createDto, UUID doctorId) {

        Clinic clinic = new Clinic();
        clinic.setName(createDto.getName());
        clinic.setPhones(createDto.getPhones());
        clinic.setEmails(createDto.getEmails());
        clinic.setAddress(createDto.getAddress());
        clinic.setNotes(createDto.getNotes());
        clinic.setDoctorId(doctorId);

        return clinic;
    }


    public Clinic update(Clinic clinic, ClinicUpdateDto updateDto) {

        clinic.setName(updateDto.getName());
        clinic.setPhones(updateDto.getPhones());
        clinic.setEmails(updateDto.getEmails());
        clinic.setAddress(updateDto.getAddress());
        clinic.setNotes(updateDto.getNotes());

        return clinic;
    }
}
