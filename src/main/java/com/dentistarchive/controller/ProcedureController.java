package com.dentistarchive.controller;

import com.dentistarchive.mapper.ProcedureMapper;
import com.dentistarchive.service.ProcedureService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Patients")
@RequestMapping("/api/v1/patients")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcedureController extends BaseController {

    ProcedureService procedureService;
    ProcedureMapper procedureMapper;

//    @GetMapping("/{id}")
//    @Operation(summary = "Get patient by id")
//    public ResponseEntity<PatientDto> getPatientById(@PathVariable UUID id) {
//        return ResponseEntity.ok(procedureMapper.toDto(procedureService.getByIdOrElseThrow(id)));
//    }
//
//    @PostMapping
//    @Operation(summary = "Create patient")
//    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientCreateDto createDto) {
//        return ResponseEntity.ok(procedureMapper.toDto(procedureService.create(createDto)));
//    }
//
//    @PatchMapping("/{id}")
//    @Operation(summary = "Update patient info")
//    public ResponseEntity<PatientDto> update(@PathVariable UUID id,
//                                             @Valid @RequestBody PatientUpdateDto updateDto) {
//        return ResponseEntity.ok(procedureMapper.toDto(procedureService.update(id, updateDto)));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable UUID id) {
//        procedureService.archiveById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{id}/restore")
//    public ResponseEntity<PatientDto> unarchive(@PathVariable UUID id) {
//        return ResponseEntity.ok(
//                procedureMapper.toDto(procedureService.unarchiveById(id))
//        );
//    }

}
