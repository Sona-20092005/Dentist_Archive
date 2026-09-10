package com.dentistarchive.controller;

import com.dentistarchive.dto.TechnicianDto;
import com.dentistarchive.dto.create.TechnicianCreateDto;
import com.dentistarchive.dto.update.TechnicianUpdateDto;
import com.dentistarchive.mapper.TechnicianMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TechnicianFilter;
import com.dentistarchive.search.sort.TechnicianSort;
import com.dentistarchive.service.TechnicianService;
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
@Tag(name = "Technician")
@RequestMapping("/api/v1/technicians")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianController extends BaseController {

    TechnicianService technicianService;
    TechnicianMapper technicianMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get technician by id")
    public ResponseEntity<TechnicianDto> getTechnicianById(@PathVariable UUID id) {
        return ResponseEntity.ok(technicianMapper.toDto(technicianService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search technician")
    public SearchResponse<TechnicianDto> search(@RequestBody SearchRequest<TechnicianFilter, TechnicianSort> searchRequest) {
        return technicianMapper.toSearchResponse(technicianService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create technician")
    public ResponseEntity<TechnicianDto> create(@Valid @RequestBody TechnicianCreateDto createDto) {
        return ResponseEntity.ok(technicianMapper.toDto(technicianService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update technician info")
    public ResponseEntity<TechnicianDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody TechnicianUpdateDto updateDto) {
        return ResponseEntity.ok(technicianMapper.toDto(technicianService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) technician")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        technicianService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) technician")
    public ResponseEntity<TechnicianDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                technicianMapper.toDto(technicianService.unarchiveById(id))
        );
    }

}
