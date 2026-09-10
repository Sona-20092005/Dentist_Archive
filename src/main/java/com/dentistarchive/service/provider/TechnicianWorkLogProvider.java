package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.TechnicianWorkLogCreateDto;
import com.dentistarchive.dto.update.TechnicianWorkLogUpdateDto;
import com.dentistarchive.entity.technician.TechnicianWorkLog;
import org.springframework.stereotype.Component;

@Component
public class TechnicianWorkLogProvider {

    public TechnicianWorkLog create(TechnicianWorkLogCreateDto createDto) {

        TechnicianWorkLog workLog = new TechnicianWorkLog();
        workLog.setRequestedDate(createDto.getRequestedDate());
        workLog.setCompletedDate(createDto.getCompletedDate());
        workLog.setUnitPrice(createDto.getUnitPrice());
        workLog.setNotes(createDto.getNotes());
        workLog.setToothNumbers(createDto.getToothNumbers());
        workLog.setTechnicianProcedureId(createDto.getTechnicianProcedureId());
        workLog.setTechnicianId(createDto.getTechnicianId());
        workLog.setQuantity(createDto.getQuantity());
        workLog.setPatientId(createDto.getPatientId());
        workLog.setIsPaid(createDto.getIsPaid());
        workLog.setPaymentDate(createDto.getPaymentDate());

        return workLog;
    }


    public TechnicianWorkLog update(TechnicianWorkLog workLog, TechnicianWorkLogUpdateDto updateDto) {

        workLog.setRequestedDate(updateDto.getRequestedDate());
        workLog.setCompletedDate(updateDto.getCompletedDate());
        workLog.setUnitPrice(updateDto.getUnitPrice());
        workLog.setNotes(updateDto.getNotes());
        workLog.setToothNumbers(updateDto.getToothNumbers());
        workLog.setTechnicianProcedureId(updateDto.getTechnicianProcedureId());
        workLog.setQuantity(updateDto.getQuantity());
        workLog.setPatientId(updateDto.getPatientId());
        workLog.setIsPaid(updateDto.getIsPaid());
        workLog.setPaymentDate(updateDto.getPaymentDate());

        return workLog;
    }
}
