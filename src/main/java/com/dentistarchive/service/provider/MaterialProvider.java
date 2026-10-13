package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.MaterialCreateDto;
import com.dentistarchive.dto.update.MaterialUpdateDto;
import com.dentistarchive.entity.material.Material;
import org.springframework.stereotype.Component;


@Component
public class MaterialProvider {

    public Material create(MaterialCreateDto createDto) {

        Material material = new Material();
        material.setName(createDto.getName());
        material.setDescription(createDto.getDescription());
        material.setNotes(createDto.getNotes());
        material.setCategoryId(createDto.getCategoryId());

        return material;
    }

    public Material update(Material material, MaterialUpdateDto updateDto) {

        material.setName(updateDto.getName());
        material.setDescription(updateDto.getDescription());
        material.setNotes(updateDto.getNotes());
        material.setCategoryId(updateDto.getCategoryId());

        return material;
    }
}
