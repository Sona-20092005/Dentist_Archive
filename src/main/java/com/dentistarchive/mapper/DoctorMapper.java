package com.dentistarchive.mapper;

import com.dentistarchive.dto.DoctorDto;
import com.dentistarchive.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = RoleMapper.class)
public abstract class DoctorMapper implements EntityMapper<Doctor, DoctorDto> {
}
