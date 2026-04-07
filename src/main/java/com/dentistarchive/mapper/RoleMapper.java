package com.dentistarchive.mapper;

import com.dentistarchive.dto.RoleDto;
import com.dentistarchive.entity.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class RoleMapper implements EntityMapper<Role, RoleDto> {

    // TODO: 4/4/2026 Delete after reading and understanding
    @AfterMapping
    protected void afterToDto(Role entity, @MappingTarget RoleDto dto) {
//        Locale locale = LocaleContextHolder.getLocale();
//        dto.setName(entity.getName(locale));
//        dto.setDescription(entity.getDescription(locale));
    }
    
}
