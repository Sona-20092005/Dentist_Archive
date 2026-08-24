package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.NurseWorkLogCreateDto;
import com.dentistarchive.dto.update.NurseWorkLogUpdateDto;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import org.springframework.stereotype.Component;


@Component
public class NurseWorkLogProvider {

    public NurseWorkLog create(NurseWorkLogCreateDto createDto) {

        NurseWorkLog nurseWorkLog = new NurseWorkLog();
        nurseWorkLog.setNurseId(createDto.getNurseId());
        nurseWorkLog.setWorkType(createDto.getWorkType());
        nurseWorkLog.setDate(createDto.getDate());
        nurseWorkLog.setStartTime(createDto.getStartTime());
        nurseWorkLog.setEndTime(createDto.getEndTime());
        nurseWorkLog.setNotes(createDto.getNotes());
        nurseWorkLog.setHourlyRate(createDto.getHourlyRate());
        return nurseWorkLog;
    }

    public NurseWorkLog update(NurseWorkLog nurseWorkLog, NurseWorkLogUpdateDto updateDto) {

        nurseWorkLog.setWorkType(updateDto.getWorkType());
        nurseWorkLog.setDate(updateDto.getDate());
        nurseWorkLog.setStartTime(updateDto.getStartTime());
        nurseWorkLog.setEndTime(updateDto.getEndTime());
        nurseWorkLog.setNotes(updateDto.getNotes());
        nurseWorkLog.setHourlyRate(updateDto.getHourlyRate());
        return nurseWorkLog;
    }
}
