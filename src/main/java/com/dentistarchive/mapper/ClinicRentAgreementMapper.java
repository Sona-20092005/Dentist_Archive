package com.dentistarchive.mapper;

import com.dentistarchive.dto.ClinicRentAgreementDto;
import com.dentistarchive.entity.rent.ClinicRentAgreement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicRentAgreementMapper implements EntityMapper<ClinicRentAgreement, ClinicRentAgreementDto> {

    public ClinicRentAgreementDto toDto(ClinicRentAgreement clinicRentAgreement) {

        ClinicRentAgreementDto dto = new ClinicRentAgreementDto();

        dto.setId(clinicRentAgreement.getId());
        dto.setCreatedAt(clinicRentAgreement.getCreatedAt());
        dto.setCreatedBy(clinicRentAgreement.getCreatedBy());
        dto.setUpdatedAt(clinicRentAgreement.getUpdatedAt());
        dto.setUpdatedBy(clinicRentAgreement.getUpdatedBy());
        dto.setArchivedAt(clinicRentAgreement.getArchivedAt());
        dto.setArchivedBy(clinicRentAgreement.getArchivedBy());
        dto.setArchived(clinicRentAgreement.isArchived());

        dto.setClinicId(clinicRentAgreement.getClinicId());
        dto.setEffectiveFrom(clinicRentAgreement.getEffectiveFrom());
        dto.setEffectiveUntil(clinicRentAgreement.getEffectiveUntil());
        dto.setRentCalculationType(clinicRentAgreement.getRentCalculationType());
        dto.setFixedMonthlyRent(clinicRentAgreement.getFixedMonthlyRent());
        dto.setHourlyRate(clinicRentAgreement.getHourlyRate());
        dto.setOvertimeHourlyRate(clinicRentAgreement.getOvertimeHourlyRate());
        dto.setCollectionPercentage(clinicRentAgreement.getCollectionPercentage());
        dto.setNotes(clinicRentAgreement.getNotes());

        return dto;
    }

}
