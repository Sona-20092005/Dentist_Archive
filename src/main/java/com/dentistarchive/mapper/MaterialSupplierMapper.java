package com.dentistarchive.mapper;

import com.dentistarchive.dto.MaterialSupplierDto;
import com.dentistarchive.entity.material.MaterialSupplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialSupplierMapper implements EntityMapper<MaterialSupplier, MaterialSupplierDto>{

    public MaterialSupplierDto toDto(MaterialSupplier materialSupplier) {

        MaterialSupplierDto dto = new MaterialSupplierDto();

        dto.setId(materialSupplier.getId());
        dto.setCreatedAt(materialSupplier.getCreatedAt());
        dto.setCreatedBy(materialSupplier.getCreatedBy());
        dto.setUpdatedAt(materialSupplier.getUpdatedAt());
        dto.setUpdatedBy(materialSupplier.getUpdatedBy());
        dto.setArchivedAt(materialSupplier.getArchivedAt());
        dto.setArchivedBy(materialSupplier.getArchivedBy());
        dto.setArchived(materialSupplier.isArchived());
        dto.setUnitOfMeasure(materialSupplier.getUnitOfMeasure());
        dto.setUnitPrice(materialSupplier.getUnitPrice());
        dto.setNotes(materialSupplier.getNotes());
        dto.setLastPriceUpdate(materialSupplier.getLastPriceUpdate());
        dto.setProductName(materialSupplier.getProductName());
        dto.setMaterialId(materialSupplier.getMaterialId());
        dto.setSupplierId(materialSupplier.getSupplierId());

        return dto;
    }
}
