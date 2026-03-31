package com.dentistarchive.controller;


import com.dentistarchive.dto.PatientDto;
import com.dentistarchive.mapper.PatientMapper;
import com.dentistarchive.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Patients")
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {

    PatientService patientService;
    PatientMapper patientMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by id")
    public PatientDto getPatientById(@PathVariable UUID id) {
        return patientMapper.toDto(patientService.getByIdOrElseThrow(id));
    }


}
