package com.dentistarchive.controller;

import com.dentistarchive.dto.PatientDto;
import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.dto.update.PatientUpdateDto;
import com.dentistarchive.mapper.PatientMapper;
import com.dentistarchive.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Patients")
@RequestMapping("/api/v1/patients")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController extends BaseController {

    PatientService patientService;
    PatientMapper patientMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by id")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.getByIdOrElseThrow(id)));
    }

    @PostMapping
    @Operation(summary = "Create patient")
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientCreateDto createDto) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.create(createDto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient info")
    public ResponseEntity<PatientDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody PatientUpdateDto updateDto) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.update(id, updateDto)));
    }

}
