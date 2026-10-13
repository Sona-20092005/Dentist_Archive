package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.MaterialSupplierCreateDto;
import com.dentistarchive.dto.update.MaterialSupplierUpdateDto;
import com.dentistarchive.entity.material.MaterialSupplier;
import org.springframework.stereotype.Component;


@Component
public class MaterialSupplierProvider {

    public MaterialSupplier create(MaterialSupplierCreateDto createDto) {

        MaterialSupplier materialSupplier = new MaterialSupplier();
        materialSupplier.setUnitOfMeasure(createDto.getUnitOfMeasure());
        materialSupplier.setUnitPrice(createDto.getUnitPrice());
        materialSupplier.setNotes(createDto.getNotes());
        materialSupplier.setLastPriceUpdate(createDto.getLastPriceUpdate());
        materialSupplier.setProductName(createDto.getProductName());
        materialSupplier.setMaterialId(createDto.getMaterialId());
        materialSupplier.setSupplierId(createDto.getSupplierId());

        return materialSupplier;
    }

    public MaterialSupplier update(MaterialSupplier materialSupplier, MaterialSupplierUpdateDto updateDto) {

        materialSupplier.setUnitOfMeasure(updateDto.getUnitOfMeasure());
        materialSupplier.setUnitPrice(updateDto.getUnitPrice());
        materialSupplier.setNotes(updateDto.getNotes());
        materialSupplier.setLastPriceUpdate(updateDto.getLastPriceUpdate());
        materialSupplier.setProductName(updateDto.getProductName());
        materialSupplier.setMaterialId(updateDto.getMaterialId());
        materialSupplier.setSupplierId(updateDto.getSupplierId());

        return materialSupplier;
    }
}
