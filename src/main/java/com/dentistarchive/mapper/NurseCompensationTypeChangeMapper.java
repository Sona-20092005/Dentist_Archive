package com.dentistarchive.mapper;

import com.dentistarchive.dto.NurseCompensationTypeChangeDto;
import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseCompensationTypeChangeMapper implements EntityMapper<NurseCompensationTypeChange, NurseCompensationTypeChangeDto>{

    public NurseCompensationTypeChangeDto toDto(NurseCompensationTypeChange nurseCompensationTypeChange) {

        NurseCompensationTypeChangeDto dto = new NurseCompensationTypeChangeDto();

        dto.setId(nurseCompensationTypeChange.getId());
        dto.setCreatedAt(nurseCompensationTypeChange.getCreatedAt());
        dto.setCreatedBy(nurseCompensationTypeChange.getCreatedBy());
        dto.setUpdatedAt(nurseCompensationTypeChange.getUpdatedAt());
        dto.setUpdatedBy(nurseCompensationTypeChange.getUpdatedBy());

        dto.setNurseId(nurseCompensationTypeChange.getNurseId());
        dto.setCompensationType(nurseCompensationTypeChange.getCompensationType());
        dto.setEffectiveFrom(nurseCompensationTypeChange.getEffectiveFrom());

        return dto;
    }
}
