package com.dentistarchive.controller;

import com.dentistarchive.dto.MaterialCategoryDto;
import com.dentistarchive.dto.create.MaterialCategoryCreateDto;
import com.dentistarchive.dto.update.MaterialCategoryUpdateDto;
import com.dentistarchive.mapper.MaterialCategoryMapper;
import com.dentistarchive.search.filter.MaterialCategoryFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.MaterialCategorySort;
import com.dentistarchive.service.MaterialCategoryService;
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
@Tag(name = "Material Category")
@RequestMapping("/api/v1/material-categories")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialCategoryController extends BaseController {

    MaterialCategoryService materialCategoryService;
    MaterialCategoryMapper materialCategoryMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get material category by id")
    public ResponseEntity<MaterialCategoryDto> getMaterialCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialCategoryMapper.toDto(materialCategoryService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search material category")
    public SearchResponse<MaterialCategoryDto> search(@RequestBody SearchRequest<MaterialCategoryFilter, MaterialCategorySort> searchRequest) {
        return materialCategoryMapper.toSearchResponse(materialCategoryService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create material category")
    public ResponseEntity<MaterialCategoryDto> create(@Valid @RequestBody MaterialCategoryCreateDto createDto) {
        return ResponseEntity.ok(materialCategoryMapper.toDto(materialCategoryService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update material category")
    public ResponseEntity<MaterialCategoryDto> update(@PathVariable UUID id,
                                                    @Valid @RequestBody MaterialCategoryUpdateDto updateDto) {
        return ResponseEntity.ok(materialCategoryMapper.toDto(materialCategoryService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) material category")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialCategoryService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) material category")
    public ResponseEntity<MaterialCategoryDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                materialCategoryMapper.toDto(materialCategoryService.unarchiveById(id))
        );
    }

}
