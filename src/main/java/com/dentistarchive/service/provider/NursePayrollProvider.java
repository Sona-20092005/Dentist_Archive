package com.dentistarchive.service.provider;

import com.dentistarchive.dto.update.NursePayrollUpdateDto;
import com.dentistarchive.entity.nurse.NursePayroll;
import org.springframework.stereotype.Component;


@Component
public class NursePayrollProvider {

    public NursePayroll update(NursePayroll nurse, NursePayrollUpdateDto updateDto) {

        nurse.setStatus(updateDto.getStatus());
        nurse.setRegularAmount(updateDto.getRegularAmount());
        nurse.setOvertimeAmount(updateDto.getOvertimeAmount());
        nurse.setBonus(updateDto.getBonus());
        nurse.setDeduction(updateDto.getDeduction());
        nurse.setPaymentDate(updateDto.getPaymentDate());
        nurse.setNotes(updateDto.getNotes());

        return nurse;
    }
}
