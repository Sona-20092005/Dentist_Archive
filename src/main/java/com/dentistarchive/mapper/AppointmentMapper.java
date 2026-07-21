package com.dentistarchive.mapper;

import com.dentistarchive.dto.AppointmentDto;
import com.dentistarchive.entity.schedule.Appointment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentMapper implements EntityMapper<Appointment, AppointmentDto>{

    public AppointmentDto toDto(Appointment appointment) {

        AppointmentDto dto = new AppointmentDto();

        dto.setId(appointment.getId());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setCreatedBy(appointment.getCreatedBy());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        dto.setUpdatedBy(appointment.getUpdatedBy());
        dto.setArchivedAt(appointment.getArchivedAt());
        dto.setArchivedBy(appointment.getArchivedBy());
        dto.setArchived(appointment.isArchived());
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setAppointmentStatus(appointment.getAppointmentStatus());
        dto.setPatientId(appointment.getPatientId());
        dto.setNurseId(appointment.getNurseId());
        dto.setNotes(appointment.getNotes());
        dto.setScheduleId(appointment.getScheduleId());

        return dto;
    }
}
