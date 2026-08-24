package com.dentistarchive.mapper;

import com.dentistarchive.dto.NursePayrollDto;
import com.dentistarchive.entity.nurse.NursePayroll;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NursePayrollMapper implements EntityMapper<NursePayroll, NursePayrollDto> {

    public NursePayrollDto toDto(NursePayroll nursePayroll) {

        NursePayrollDto dto = new NursePayrollDto();

        dto.setId(nursePayroll.getId());
        dto.setCreatedAt(nursePayroll.getCreatedAt());
        dto.setCreatedBy(nursePayroll.getCreatedBy());
        dto.setUpdatedAt(nursePayroll.getUpdatedAt());
        dto.setUpdatedBy(nursePayroll.getUpdatedBy());

        dto.setNurseId(nursePayroll.getNurseId());
        dto.setStatus(nursePayroll.getStatus());
        dto.setYear(nursePayroll .getYear());
        dto.setMonth(nursePayroll.getMonth());
        dto.setCalculatedRegularAmount(nursePayroll.getCalculatedRegularAmount());
        dto.setRegularAmount(nursePayroll.getRegularAmount());
        dto.setCalculatedOvertimeAmount(nursePayroll.getCalculatedOvertimeAmount());
        dto.setOvertimeAmount(nursePayroll.getOvertimeAmount());
        dto.setCompensationType(nursePayroll.getCompensationType());
        dto.setBonus(nursePayroll.getBonus());
        dto.setDeduction(nursePayroll.getDeduction());
        dto.setRegularMinutesWorked(nursePayroll.getRegularMinutesWorked());
        dto.setOvertimeMinutesWorked(nursePayroll.getOvertimeMinutesWorked());
        dto.setPaymentDate(nursePayroll.getPaymentDate());
        dto.setLocked(nursePayroll.getLocked());
        dto.setNotes(nursePayroll.getNotes());
        dto.setIsManuallyAdjusted(nursePayroll.isManuallyAdjusted());

        return dto;
    }

}
