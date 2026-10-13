package com.dentistarchive.mapper;

import com.dentistarchive.dto.SupplierDto;
import com.dentistarchive.entity.material.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierMapper implements EntityMapper<Supplier, SupplierDto>{

    public SupplierDto toDto(Supplier supplier) {

        SupplierDto dto = new SupplierDto();

        dto.setId(supplier.getId());
        dto.setCreatedAt(supplier.getCreatedAt());
        dto.setCreatedBy(supplier.getCreatedBy());
        dto.setUpdatedAt(supplier.getUpdatedAt());
        dto.setUpdatedBy(supplier.getUpdatedBy());
        dto.setArchivedAt(supplier.getArchivedAt());
        dto.setArchivedBy(supplier.getArchivedBy());
        dto.setArchived(supplier.isArchived());
        dto.setName(supplier.getName());
        dto.setPhones(supplier.getPhones());
        dto.setEmails(supplier.getEmails());
        dto.setAddresses(supplier.getAddresses());
        dto.setWebsite(supplier.getWebsite());
        dto.setNotes(supplier.getNotes());
        dto.setDoctorId(supplier.getDoctorId());

        return dto;
    }
}
