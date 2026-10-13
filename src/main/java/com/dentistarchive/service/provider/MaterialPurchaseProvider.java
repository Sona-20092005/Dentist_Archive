package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.MaterialPurchaseCreateDto;
import com.dentistarchive.dto.update.MaterialPurchaseUpdateDto;
import com.dentistarchive.entity.material.MaterialPurchase;
import org.springframework.stereotype.Component;


@Component
public class MaterialPurchaseProvider {

    public MaterialPurchase create(MaterialPurchaseCreateDto createDto) {

        MaterialPurchase materialPurchase = new MaterialPurchase();

        materialPurchase.setTotalAmount(createDto.getTotalAmount());
        materialPurchase.setUnitPrice(createDto.getUnitPrice());
        materialPurchase.setNotes(createDto.getNotes());
        materialPurchase.setQuantity(createDto.getQuantity());
        materialPurchase.setPurchaseDate(createDto.getPurchaseDate());
        materialPurchase.setMaterialSupplierId(createDto.getMaterialSupplierId());

        return materialPurchase;
    }

    public MaterialPurchase update(MaterialPurchase materialPurchase, MaterialPurchaseUpdateDto updateDto) {

        materialPurchase.setTotalAmount(updateDto.getTotalAmount());
        materialPurchase.setUnitPrice(updateDto.getUnitPrice());
        materialPurchase.setNotes(updateDto.getNotes());
        materialPurchase.setQuantity(updateDto.getQuantity());
        materialPurchase.setPurchaseDate(updateDto.getPurchaseDate());
        materialPurchase.setMaterialSupplierId(updateDto.getMaterialSupplierId());

        return materialPurchase;
    }
}
