package com.dentistarchive.mapper;

import com.dentistarchive.dto.RentOvertimeDto;
import com.dentistarchive.entity.rent.RentOvertime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentOvertimeMapper implements EntityMapper<RentOvertime, RentOvertimeDto>{

    public RentOvertimeDto toDto(RentOvertime rentOvertime) {

        RentOvertimeDto dto = new RentOvertimeDto();

        dto.setId(rentOvertime.getId());
        dto.setCreatedAt(rentOvertime.getCreatedAt());
        dto.setCreatedBy(rentOvertime.getCreatedBy());
        dto.setUpdatedAt(rentOvertime.getUpdatedAt());
        dto.setUpdatedBy(rentOvertime.getUpdatedBy());
        dto.setArchivedAt(rentOvertime.getArchivedAt());
        dto.setArchivedBy(rentOvertime.getArchivedBy());
        dto.setArchived(rentOvertime.isArchived());

        dto.setLocked(rentOvertime.getLocked());
        dto.setWorkScheduleModificationId(rentOvertime.getWorkScheduleModificationId());
        dto.setHourlyRent(rentOvertime.getHourlyRent());
        dto.setNotes(rentOvertime.getNotes());

        return dto;
    }
}
