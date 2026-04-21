package com.dentistarchive.dto;

import com.dentistarchive.enums.Role;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DoctorDto.class)
})
public abstract sealed class UserDto extends ArchivingBaseDto permits DoctorDto {

    @Schema(description = "Full name (not unique)")
    String fullName;

    @Schema(description = "User role")
    Role role;


    boolean temporaryPassword;

    Map<String, String> additionalParams;
}
