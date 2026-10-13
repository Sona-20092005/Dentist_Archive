package com.dentistarchive.controller;

import com.dentistarchive.dto.MaterialSupplierDto;
import com.dentistarchive.dto.create.MaterialSupplierCreateDto;
import com.dentistarchive.dto.update.MaterialSupplierUpdateDto;
import com.dentistarchive.mapper.MaterialSupplierMapper;
import com.dentistarchive.search.filter.MaterialSupplierFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.MaterialSupplierSort;
import com.dentistarchive.service.MaterialSupplierService;
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
@Tag(name = "Material Supplier")
@RequestMapping("/api/v1/material-supplier")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialSupplierController extends BaseController {

    MaterialSupplierService materialSupplierService;
    MaterialSupplierMapper materialSupplierMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get material supplier by id")
    public ResponseEntity<MaterialSupplierDto> getMaterialSupplierById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialSupplierMapper.toDto(materialSupplierService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search material suppliers")
    public SearchResponse<MaterialSupplierDto> search(@RequestBody SearchRequest<MaterialSupplierFilter, MaterialSupplierSort> searchRequest) {
        return materialSupplierMapper.toSearchResponse(materialSupplierService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create material supplier")
    public ResponseEntity<MaterialSupplierDto> create(@Valid @RequestBody MaterialSupplierCreateDto createDto) {
        return ResponseEntity.ok(materialSupplierMapper.toDto(materialSupplierService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update material supplier")
    public ResponseEntity<MaterialSupplierDto> update(@PathVariable UUID id,
                                                    @Valid @RequestBody MaterialSupplierUpdateDto updateDto) {
        return ResponseEntity.ok(materialSupplierMapper.toDto(materialSupplierService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) material supplier")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialSupplierService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) material supplier")
    public ResponseEntity<MaterialSupplierDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                materialSupplierMapper.toDto(materialSupplierService.unarchiveById(id))
        );
    }

}
