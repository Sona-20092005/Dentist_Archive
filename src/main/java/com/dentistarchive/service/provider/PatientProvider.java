package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.entity.patient.Patient;
import org.springframework.stereotype.Component;


@Component
public class PatientProvider {

    public Patient create(PatientCreateDto createDto) {

        Patient patient = new Patient();
        patient.setName(createDto.getName());
        patient.setPhones(createDto.getPhones());
        patient.setEmails(createDto.getEmails());
        patient.setAddress(createDto.getAddress());
        patient.setPassportInformation(createDto.getPassportInformation());
        patient.setNotes(createDto.getNotes());

        return patient;
    }
}
