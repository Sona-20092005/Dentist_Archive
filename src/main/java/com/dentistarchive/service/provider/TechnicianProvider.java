package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.TechnicianCreateDto;
import com.dentistarchive.dto.update.TechnicianUpdateDto;
import com.dentistarchive.entity.technician.Technician;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class TechnicianProvider {

    public Technician create(TechnicianCreateDto createDto, UUID doctorId) {

        Technician technician = new Technician();
        technician.setName(createDto.getName());
        technician.setPhones(createDto.getPhones());
        technician.setEmails(createDto.getEmails());
        technician.setNotes(createDto.getNotes());
        technician.setAddress(createDto.getAddress());
        technician.setCollaborationStartDate(createDto.getCollaborationStartDate());
        technician.setCollaborationEndDate(createDto.getCollaborationEndDate());
        technician.setMonthlyReportStartDate(createDto.getMonthlyReportStartDate());
        technician.setDoctorId(doctorId);

        return technician;
    }


    public Technician update(Technician technician, TechnicianUpdateDto updateDto) {

        technician.setName(updateDto.getName());
        technician.setPhones(updateDto.getPhones());
        technician.setEmails(updateDto.getEmails());
        technician.setNotes(updateDto.getNotes());
        technician.setAddress(updateDto.getAddress());
        technician.setCollaborationStartDate(updateDto.getCollaborationStartDate());
        technician.setCollaborationEndDate(updateDto.getCollaborationEndDate());
        technician.setMonthlyReportStartDate(updateDto.getMonthlyReportStartDate());

        return technician;
    }
}
