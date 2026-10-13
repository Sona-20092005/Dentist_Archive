package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.SupplierCreateDto;
import com.dentistarchive.dto.update.SupplierUpdateDto;
import com.dentistarchive.entity.material.Supplier;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class SupplierProvider {

    public Supplier create(SupplierCreateDto createDto, UUID doctorId) {

        Supplier supplier = new Supplier();
        supplier.setName(createDto.getName());
        supplier.setPhones(createDto.getPhones());
        supplier.setEmails(createDto.getEmails());
        supplier.setAddresses(createDto.getAddresses());
        supplier.setWebsite(createDto.getWebsite());
        supplier.setNotes(createDto.getNotes());
        supplier.setDoctorId(doctorId);

        return supplier;
    }

    public Supplier update(Supplier supplier, SupplierUpdateDto updateDto) {

        supplier.setName(updateDto.getName());
        supplier.setPhones(updateDto.getPhones());
        supplier.setEmails(updateDto.getEmails());
        supplier.setAddresses(updateDto.getAddresses());
        supplier.setWebsite(updateDto.getWebsite());
        supplier.setNotes(updateDto.getNotes());

        return supplier;
    }
}
