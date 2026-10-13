package com.dentistarchive.mapper;

import com.dentistarchive.dto.MaterialPurchaseDto;
import com.dentistarchive.entity.material.MaterialPurchase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialPurchaseMapper implements EntityMapper<MaterialPurchase, MaterialPurchaseDto>{

    public MaterialPurchaseDto toDto(MaterialPurchase purchase) {

        MaterialPurchaseDto dto = new MaterialPurchaseDto();

        dto.setId(purchase.getId());
        dto.setCreatedAt(purchase.getCreatedAt());
        dto.setCreatedBy(purchase.getCreatedBy());
        dto.setUpdatedAt(purchase.getUpdatedAt());
        dto.setUpdatedBy(purchase.getUpdatedBy());
        dto.setArchivedAt(purchase.getArchivedAt());
        dto.setArchivedBy(purchase.getArchivedBy());
        dto.setArchived(purchase.isArchived());
        dto.setTotalAmount(purchase.getTotalAmount());
        dto.setUnitPrice(purchase.getUnitPrice());
        dto.setNotes(purchase.getNotes());
        dto.setQuantity(purchase.getQuantity());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        dto.setMaterialSupplierId(purchase.getMaterialSupplierId());

        return dto;
    }
}
