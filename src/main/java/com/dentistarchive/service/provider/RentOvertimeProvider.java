package com.dentistarchive.service.provider;

import com.dentistarchive.dto.update.RentOvertimeUpdateDto;
import com.dentistarchive.entity.rent.RentOvertime;
import org.springframework.stereotype.Component;


@Component
public class RentOvertimeProvider {

    public RentOvertime update(RentOvertime rentOvertime, RentOvertimeUpdateDto updateDto) {

        rentOvertime.setHourlyRent(updateDto.getHourlyRent());
        rentOvertime.setNotes(updateDto.getNotes());

        return rentOvertime;
    }
}
