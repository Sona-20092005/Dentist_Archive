package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.dto.update.PatientUpdateDto;
import com.dentistarchive.entity.patient.Patient;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class PatientProvider {

    public Patient create(PatientCreateDto createDto, UUID doctorId) {

        Patient patient = new Patient();
        patient.setName(createDto.getName());
        patient.setPhones(createDto.getPhones());
        patient.setEmails(createDto.getEmails());
        patient.setAddress(createDto.getAddress());
        patient.setPassportInformation(createDto.getPassportInformation());
        patient.setNotes(createDto.getNotes());
        patient.setDoctorId(doctorId);

        return patient;
    }


    public Patient update(Patient patient, PatientUpdateDto updateDto) {

        patient.setName(updateDto.getName());
        patient.setPhones(updateDto.getPhones());
        patient.setEmails(updateDto.getEmails());
        patient.setAddress(updateDto.getAddress());
        patient.setPassportInformation(updateDto.getPassportInformation());
        patient.setNotes(updateDto.getNotes());

        return patient;
    }
}
