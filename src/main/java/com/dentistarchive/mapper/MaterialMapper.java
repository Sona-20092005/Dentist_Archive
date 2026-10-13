package com.dentistarchive.mapper;

import com.dentistarchive.dto.MaterialDto;
import com.dentistarchive.entity.material.Material;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialMapper implements EntityMapper<Material, MaterialDto>{

    public MaterialDto toDto(Material material) {

        MaterialDto dto = new MaterialDto();

        dto.setId(material.getId());
        dto.setCreatedAt(material.getCreatedAt());
        dto.setCreatedBy(material.getCreatedBy());
        dto.setUpdatedAt(material.getUpdatedAt());
        dto.setUpdatedBy(material.getUpdatedBy());
        dto.setArchivedAt(material.getArchivedAt());
        dto.setArchivedBy(material.getArchivedBy());
        dto.setArchived(material.isArchived());
        dto.setName(material.getName());
        dto.setDescription(material.getDescription());
        dto.setNotes(material.getNotes());
        dto.setCategoryId(material.getCategoryId());

        return dto;
    }
}
