package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.NurseCreateDto;
import com.dentistarchive.dto.update.NurseUpdateDto;
import com.dentistarchive.entity.nurse.Nurse;
import org.springframework.stereotype.Component;


@Component
public class NurseProvider {

    public Nurse create(NurseCreateDto createDto) {

        Nurse nurse = new Nurse();
        nurse.setName(createDto.getName());
        nurse.setPhones(createDto.getPhones());
        nurse.setEmails(createDto.getEmails());
        nurse.setNotes(createDto.getNotes());
        nurse.setClinicId(createDto.getClinicId());
        nurse.setBaseSalary(createDto.getBaseSalary());
        nurse.setHourlyRate(createDto.getHourlyRate());
        nurse.setOvertimeHourlyRate(createDto.getOvertimeHourlyRate());
        nurse.setHireDate(createDto.getHireDate());
        nurse.setTerminationDate(createDto.getTerminationDate());
        nurse.setPayrollStartDate(createDto.getPayrollStartDate());
        nurse.setContractedWeeklyHours(createDto.getContractedWeeklyHours());
        nurse.setCompensationType(createDto.getCompensationType());

        return nurse;
    }


    public Nurse update(Nurse nurse, NurseUpdateDto updateDto) {

        nurse.setName(updateDto.getName());
        nurse.setPhones(updateDto.getPhones());
        nurse.setEmails(updateDto.getEmails());
        nurse.setNotes(updateDto.getNotes());
        nurse.setClinicId(updateDto.getClinicId());
        nurse.setBaseSalary(updateDto.getBaseSalary());
        nurse.setHourlyRate(updateDto.getHourlyRate());
        nurse.setOvertimeHourlyRate(updateDto.getOvertimeHourlyRate());
        nurse.setHireDate(updateDto.getHireDate());
        nurse.setTerminationDate(updateDto.getTerminationDate());
        nurse.setPayrollStartDate(updateDto.getPayrollStartDate());
        nurse.setContractedWeeklyHours(updateDto.getContractedWeeklyHours());
        nurse.setCompensationType(updateDto.getCompensationType());


        return nurse;
    }
}
