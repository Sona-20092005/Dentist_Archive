package com.dentistarchive.controller;

import com.dentistarchive.dto.MaterialPurchaseDto;
import com.dentistarchive.dto.create.MaterialPurchaseCreateDto;
import com.dentistarchive.dto.update.MaterialPurchaseUpdateDto;
import com.dentistarchive.mapper.MaterialPurchaseMapper;
import com.dentistarchive.search.filter.MaterialPurchaseFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.MaterialPurchaseSort;
import com.dentistarchive.service.MaterialPurchaseService;
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
@Tag(name = "Material Purchase")
@RequestMapping("/api/v1/material-purchases")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialPurchaseController extends BaseController {

    MaterialPurchaseService materialPurchaseService;
    MaterialPurchaseMapper materialPurchaseMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get material purchase by id")
    public ResponseEntity<MaterialPurchaseDto> getMaterialPurchaseById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialPurchaseMapper.toDto(materialPurchaseService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search material purchases")
    public SearchResponse<MaterialPurchaseDto> search(@RequestBody SearchRequest<MaterialPurchaseFilter, MaterialPurchaseSort> searchRequest) {
        return materialPurchaseMapper.toSearchResponse(materialPurchaseService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create material purchase")
    public ResponseEntity<MaterialPurchaseDto> create(@Valid @RequestBody MaterialPurchaseCreateDto createDto) {
        return ResponseEntity.ok(materialPurchaseMapper.toDto(materialPurchaseService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update material purchase")
    public ResponseEntity<MaterialPurchaseDto> update(@PathVariable UUID id,
                                                    @Valid @RequestBody MaterialPurchaseUpdateDto updateDto) {
        return ResponseEntity.ok(materialPurchaseMapper.toDto(materialPurchaseService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) material purchase")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialPurchaseService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) material purchase")
    public ResponseEntity<MaterialPurchaseDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                materialPurchaseMapper.toDto(materialPurchaseService.unarchiveById(id))
        );
    }

}
