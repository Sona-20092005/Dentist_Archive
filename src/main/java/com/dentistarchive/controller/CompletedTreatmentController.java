package com.dentistarchive.controller;

import com.dentistarchive.dto.CompletedTreatmentDto;
import com.dentistarchive.dto.create.CompletedTreatmentCreateDto;
import com.dentistarchive.dto.update.CompletedTreatmentUpdateDto;
import com.dentistarchive.mapper.CompletedTreatmentMapper;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.CompletedTreatmentSort;
import com.dentistarchive.service.CompletedTreatmentService;
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
@Tag(name = "Completed Treatment")
@RequestMapping("/api/v1/completed-treatment")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompletedTreatmentController extends BaseController {

    CompletedTreatmentService treatmentService;
    CompletedTreatmentMapper treatmentMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get treatment by id")
    public ResponseEntity<CompletedTreatmentDto> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(treatmentMapper.toDto(treatmentService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search treatment")
    public SearchResponse<CompletedTreatmentDto> search(@RequestBody SearchRequest<CompletedTreatmentFilter, CompletedTreatmentSort> searchRequest) {
        return treatmentMapper.toSearchResponse(treatmentService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create treatment")
    public ResponseEntity<CompletedTreatmentDto> create(@Valid @RequestBody CompletedTreatmentCreateDto createDto) {
        return ResponseEntity.ok(treatmentMapper.toDto(treatmentService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update treatment info")
    public ResponseEntity<CompletedTreatmentDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody CompletedTreatmentUpdateDto updateDto) {
        return ResponseEntity.ok(treatmentMapper.toDto(treatmentService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) treatment")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        treatmentService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) treatment")
    public ResponseEntity<CompletedTreatmentDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                treatmentMapper.toDto(treatmentService.unarchiveById(id))
        );
    }

}
