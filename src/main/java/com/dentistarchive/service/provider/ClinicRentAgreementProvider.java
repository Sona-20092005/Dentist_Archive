package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.ClinicRentAgreementCreateDto;
import com.dentistarchive.dto.update.ClinicRentAgreementUpdateDto;
import com.dentistarchive.entity.rent.ClinicRentAgreement;
import org.springframework.stereotype.Component;


@Component
public class ClinicRentAgreementProvider {

    public ClinicRentAgreement create(ClinicRentAgreementCreateDto createDto) {

        ClinicRentAgreement clinicRentAgreement = new ClinicRentAgreement();
        clinicRentAgreement.setClinicId(createDto.getClinicId());
        clinicRentAgreement.setEffectiveFrom(createDto.getEffectiveFrom());
        clinicRentAgreement.setEffectiveUntil(createDto.getEffectiveUntil());
        clinicRentAgreement.setRentCalculationType(createDto.getRentCalculationType());
        clinicRentAgreement.setFixedMonthlyRent(createDto.getFixedMonthlyRent());
        clinicRentAgreement.setHourlyRate(createDto.getHourlyRate());
        clinicRentAgreement.setOvertimeHourlyRate(createDto.getOvertimeHourlyRate());
        clinicRentAgreement.setCollectionPercentage(createDto.getCollectionPercentage());
        clinicRentAgreement.setNotes(createDto.getNotes());

        return clinicRentAgreement;
    }


    public ClinicRentAgreement update(ClinicRentAgreement clinicRentAgreement, ClinicRentAgreementUpdateDto updateDto) {

        clinicRentAgreement.setClinicId(updateDto.getClinicId());
        clinicRentAgreement.setEffectiveFrom(updateDto.getEffectiveFrom());
        clinicRentAgreement.setEffectiveUntil(updateDto.getEffectiveUntil());
        clinicRentAgreement.setRentCalculationType(updateDto.getRentCalculationType());
        clinicRentAgreement.setFixedMonthlyRent(updateDto.getFixedMonthlyRent());
        clinicRentAgreement.setHourlyRate(updateDto.getHourlyRate());
        clinicRentAgreement.setOvertimeHourlyRate(updateDto.getOvertimeHourlyRate());
        clinicRentAgreement.setCollectionPercentage(updateDto.getCollectionPercentage());
        clinicRentAgreement.setNotes(updateDto.getNotes());

        return clinicRentAgreement;
    }
}
