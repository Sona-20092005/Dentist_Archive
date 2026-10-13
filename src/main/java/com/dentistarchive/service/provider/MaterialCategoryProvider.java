package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.MaterialCategoryCreateDto;
import com.dentistarchive.dto.update.MaterialCategoryUpdateDto;
import com.dentistarchive.entity.material.MaterialCategory;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class MaterialCategoryProvider {

    public MaterialCategory create(MaterialCategoryCreateDto createDto, UUID doctorId) {

        MaterialCategory materialCategory = new MaterialCategory();
        materialCategory.setName(createDto.getName());
        materialCategory.setDescription(createDto.getDescription());
        materialCategory.setDoctorId(doctorId);

        return materialCategory;
    }

    public MaterialCategory update(MaterialCategory materialCategory, MaterialCategoryUpdateDto updateDto) {

        materialCategory.setName(updateDto.getName());
        materialCategory.setDescription(updateDto.getDescription());

        return materialCategory;
    }
}
