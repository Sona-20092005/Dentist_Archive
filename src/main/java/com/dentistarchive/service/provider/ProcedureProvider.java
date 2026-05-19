package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.ProcedureCreateDto;
import com.dentistarchive.dto.update.ProcedureUpdateDto;
import com.dentistarchive.entity.pricelist.Procedure;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class ProcedureProvider {

    public Procedure create(ProcedureCreateDto createDto, UUID doctorId) {

        Procedure procedure = new Procedure();
        procedure.setName(createDto.getName());
        procedure.setPrice(createDto.getPrice());
        procedure.setDescription(createDto.getDescription());
        procedure.setDoctorId(doctorId);

        return procedure;
    }


    public Procedure update(Procedure procedure, ProcedureUpdateDto updateDto) {

        procedure.setName(updateDto.getName());
        procedure.setPrice(updateDto.getPrice());
        procedure.setDescription(updateDto.getDescription());

        return procedure;
    }
}
