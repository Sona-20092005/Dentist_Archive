package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.TechnicianProcedureCreateDto;
import com.dentistarchive.dto.update.TechnicianProcedureUpdateDto;
import com.dentistarchive.entity.technician.TechnicianProcedure;
import org.springframework.stereotype.Component;


@Component
public class TechnicianProcedureProvider {

    public TechnicianProcedure create(TechnicianProcedureCreateDto createDto) {

        TechnicianProcedure procedure = new TechnicianProcedure();
        procedure.setName(createDto.getName());
        procedure.setPrice(createDto.getPrice());
        procedure.setDescription(createDto.getDescription());
        procedure.setTechnicianId(createDto.getTechnicianId());

        return procedure;
    }


    public TechnicianProcedure update(TechnicianProcedure procedure, TechnicianProcedureUpdateDto updateDto) {

        procedure.setName(updateDto.getName());
        procedure.setPrice(updateDto.getPrice());
        procedure.setDescription(updateDto.getDescription());
        procedure.setTechnicianId(updateDto.getTechnicianId());

        return procedure;
    }
}
