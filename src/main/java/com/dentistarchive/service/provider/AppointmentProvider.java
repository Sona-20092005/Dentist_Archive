package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.AppointmentCreateDto;
import com.dentistarchive.dto.update.AppointmentUpdateDto;
import com.dentistarchive.entity.schedule.Appointment;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class AppointmentProvider {

    public Appointment create(AppointmentCreateDto createDto, UUID doctorId) {

        Appointment appointment = new Appointment();
        appointment.setDate(createDto.getDate());
        appointment.setStartTime(createDto.getStartTime());
        appointment.setEndTime(createDto.getEndTime());
        appointment.setAppointmentStatus(createDto.getAppointmentStatus());
        appointment.setNotes(createDto.getNotes());
        appointment.setPatientId(createDto.getPatientId());
        appointment.setNurseId(createDto.getNurseId());
        appointment.setDoctorId(doctorId);

        return appointment;
    }


    public Appointment update(Appointment appointment, AppointmentUpdateDto updateDto) {

        appointment.setDate(updateDto.getDate());
        appointment.setStartTime(updateDto.getStartTime());
        appointment.setEndTime(updateDto.getEndTime());
        appointment.setAppointmentStatus(updateDto.getAppointmentStatus());
        appointment.setNotes(updateDto.getNotes());
        appointment.setPatientId(updateDto.getPatientId());
        appointment.setNurseId(updateDto.getNurseId());

        return appointment;
    }
}
