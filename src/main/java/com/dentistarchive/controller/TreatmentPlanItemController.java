package com.dentistarchive.controller;

import com.dentistarchive.dto.TreatmentPlanItemDto;
import com.dentistarchive.dto.create.TreatmentPlanItemCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanItemUpdateDto;
import com.dentistarchive.mapper.TreatmentPlanItemMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import com.dentistarchive.search.sort.TreatmentPlanItemSort;
import com.dentistarchive.service.TreatmentPlanItemService;
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
@Tag(name = "Treatment Plan Item")
@RequestMapping("/api/v1/treatment-plan-item")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanItemController extends BaseController {

    TreatmentPlanItemService itemService;
    TreatmentPlanItemMapper itemMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get plan item by id")
    public ResponseEntity<TreatmentPlanItemDto> getPlanItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemMapper.toDto(itemService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search plan item")
    public SearchResponse<TreatmentPlanItemDto> search(@RequestBody SearchRequest<TreatmentPlanItemFilter, TreatmentPlanItemSort> searchRequest) {
        return itemMapper.toSearchResponse(itemService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create plan item")
    public ResponseEntity<TreatmentPlanItemDto> create(@Valid @RequestBody TreatmentPlanItemCreateDto createDto) {
        return ResponseEntity.ok(itemMapper.toDto(itemService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update plan item info")
    public ResponseEntity<TreatmentPlanItemDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody TreatmentPlanItemUpdateDto updateDto) {
        return ResponseEntity.ok(itemMapper.toDto(itemService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) plan item")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) plan item")
    public ResponseEntity<TreatmentPlanItemDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                itemMapper.toDto(itemService.unarchiveById(id))
        );
    }

}
