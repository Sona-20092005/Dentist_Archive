package com.dentistarchive.controller;

import com.dentistarchive.dto.PatientDto;
import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.dto.update.PatientUpdateDto;
import com.dentistarchive.mapper.PatientMapper;
import com.dentistarchive.search.filter.PatientFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.PatientSort;
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

    @PostMapping("/search")
    @Operation(summary = "Search patient")
    public SearchResponse<PatientDto> search(@RequestBody SearchRequest<PatientFilter, PatientSort> searchRequest) {
        return patientMapper.toSearchResponse(patientService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create patient")
    public ResponseEntity<PatientDto> create(@Valid @RequestBody PatientCreateDto createDto) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update patient info")
    public ResponseEntity<PatientDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody PatientUpdateDto updateDto) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.update(id, updateDto)));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate patient (make status inactive)")
    public ResponseEntity<PatientDto> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(
                patientMapper.toDto(patientService.deactivate(id))
        );
    }

    @PostMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate patient (restore status)")
    public ResponseEntity<PatientDto> reactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(
                patientMapper.toDto(patientService.reactivate(id))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) patient")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) patient")
    public ResponseEntity<PatientDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                patientMapper.toDto(patientService.unarchiveById(id))
        );
    }

}
