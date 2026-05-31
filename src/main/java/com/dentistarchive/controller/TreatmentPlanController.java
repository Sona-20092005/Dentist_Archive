package com.dentistarchive.controller;

import com.dentistarchive.dto.TreatmentPlanDto;
import com.dentistarchive.dto.create.TreatmentPlanCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanUpdateDto;
import com.dentistarchive.mapper.TreatmentPlanMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.search.sort.TreatmentPlanSort;
import com.dentistarchive.service.TreatmentPlanService;
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
@Tag(name = "Treatment Plan")
@RequestMapping("/api/v1/treatment-plan")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanController extends BaseController {

    TreatmentPlanService planService;
    TreatmentPlanMapper planMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get plan by id")
    public ResponseEntity<TreatmentPlanDto> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(planMapper.toDto(planService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search plan")
    public SearchResponse<TreatmentPlanDto> search(@RequestBody SearchRequest<TreatmentPlanFilter, TreatmentPlanSort> searchRequest) {
        return planMapper.toSearchResponse(planService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create plan")
    public ResponseEntity<TreatmentPlanDto> create(@Valid @RequestBody TreatmentPlanCreateDto createDto) {
        return ResponseEntity.ok(planMapper.toDto(planService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update plan info")
    public ResponseEntity<TreatmentPlanDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody TreatmentPlanUpdateDto updateDto) {
        return ResponseEntity.ok(planMapper.toDto(planService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) plan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        planService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) plan")
    public ResponseEntity<TreatmentPlanDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                planMapper.toDto(planService.unarchiveById(id))
        );
    }

}
