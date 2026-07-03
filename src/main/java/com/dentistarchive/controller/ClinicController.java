package com.dentistarchive.controller;

import com.dentistarchive.dto.ClinicDto;
import com.dentistarchive.dto.create.ClinicCreateDto;
import com.dentistarchive.dto.update.ClinicUpdateDto;
import com.dentistarchive.mapper.ClinicMapper;
import com.dentistarchive.search.filter.ClinicFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.ClinicSort;
import com.dentistarchive.service.ClinicService;
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
@Tag(name = "Clinic")
@RequestMapping("/api/v1/clinics")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicController extends BaseController {

    ClinicService clinicService;
    ClinicMapper clinicMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get clinic by id")
    public ResponseEntity<ClinicDto> getClinicById(@PathVariable UUID id) {
        return ResponseEntity.ok(clinicMapper.toDto(clinicService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search clinic")
    public SearchResponse<ClinicDto> search(@RequestBody SearchRequest<ClinicFilter, ClinicSort> searchRequest) {
        return clinicMapper.toSearchResponse(clinicService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create clinic")
    public ResponseEntity<ClinicDto> create(@Valid @RequestBody ClinicCreateDto createDto) {
        return ResponseEntity.ok(clinicMapper.toDto(clinicService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update clinic info")
    public ResponseEntity<ClinicDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody ClinicUpdateDto updateDto) {
        return ResponseEntity.ok(clinicMapper.toDto(clinicService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) clinic")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clinicService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) clinic")
    public ResponseEntity<ClinicDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                clinicMapper.toDto(clinicService.unarchiveById(id))
        );
    }

}
