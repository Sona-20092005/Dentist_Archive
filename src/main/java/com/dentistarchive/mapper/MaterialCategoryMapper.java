package com.dentistarchive.mapper;

import com.dentistarchive.dto.MaterialCategoryDto;
import com.dentistarchive.entity.material.MaterialCategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialCategoryMapper implements EntityMapper<MaterialCategory, MaterialCategoryDto>{

    public MaterialCategoryDto toDto(MaterialCategory category) {

        MaterialCategoryDto dto = new MaterialCategoryDto();

        dto.setId(category.getId());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setUpdatedAt(category.getUpdatedAt());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setArchivedAt(category.getArchivedAt());
        dto.setArchivedBy(category.getArchivedBy());
        dto.setArchived(category.isArchived());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setDoctorId(category.getDoctorId());

        return dto;
    }
}
