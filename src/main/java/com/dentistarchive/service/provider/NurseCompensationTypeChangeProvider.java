package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.NurseCompensationTypeChangeCreateDto;
import com.dentistarchive.dto.update.NurseCompensationTypeChangeUpdateDto;
import com.dentistarchive.entity.nurse.NurseCompensationTypeChange;
import org.springframework.stereotype.Component;


@Component
public class NurseCompensationTypeChangeProvider {

    public NurseCompensationTypeChange create(NurseCompensationTypeChangeCreateDto createDto) {

        NurseCompensationTypeChange nurseCompensationTypeChange = new NurseCompensationTypeChange();
        nurseCompensationTypeChange.setNurseId(createDto.getNurseId());
        nurseCompensationTypeChange.setCompensationType(createDto.getCompensationType());
        nurseCompensationTypeChange.setEffectiveFrom(createDto.getEffectiveFrom());

        return nurseCompensationTypeChange;
    }

    public NurseCompensationTypeChange update(NurseCompensationTypeChange nurseCompensationTypeChange, NurseCompensationTypeChangeUpdateDto updateDto) {

        nurseCompensationTypeChange.setEffectiveFrom(updateDto.getEffectiveFrom());

        return nurseCompensationTypeChange;
    }
}

