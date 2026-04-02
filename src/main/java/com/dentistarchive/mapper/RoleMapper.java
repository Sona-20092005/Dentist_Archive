package com.dentistarchive.mapper;

import com.dentistarchive.dto.RoleDto;
import com.dentistarchive.entity.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Mapper(componentModel = "spring")
public abstract class RoleMapper implements EntityMapper<Role, RoleDto> {

    @AfterMapping
    protected void afterToDto(Role entity, @MappingTarget RoleDto dto) {
        Locale locale = LocaleContextHolder.getLocale();
        dto.setName(entity.getName(locale));
        dto.setDescription(entity.getDescription(locale));
    }
}
