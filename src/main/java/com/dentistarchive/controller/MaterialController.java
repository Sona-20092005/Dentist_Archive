package com.dentistarchive.controller;

import com.dentistarchive.dto.MaterialDto;
import com.dentistarchive.dto.create.MaterialCreateDto;
import com.dentistarchive.dto.update.MaterialUpdateDto;
import com.dentistarchive.mapper.MaterialMapper;
import com.dentistarchive.search.filter.MaterialFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.MaterialSort;
import com.dentistarchive.service.MaterialService;
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
@Tag(name = "Material")
@RequestMapping("/api/v1/materials")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialController extends BaseController {

    MaterialService materialService;
    MaterialMapper materialMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get material by id")
    public ResponseEntity<MaterialDto> getMaterialId(@PathVariable UUID id) {
        return ResponseEntity.ok(materialMapper.toDto(materialService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search material")
    public SearchResponse<MaterialDto> search(@RequestBody SearchRequest<MaterialFilter, MaterialSort> searchRequest) {
        return materialMapper.toSearchResponse(materialService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create material")
    public ResponseEntity<MaterialDto> create(@Valid @RequestBody MaterialCreateDto createDto) {
        return ResponseEntity.ok(materialMapper.toDto(materialService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update material")
    public ResponseEntity<MaterialDto> update(@PathVariable UUID id,
                                            @Valid @RequestBody MaterialUpdateDto updateDto) {
        return ResponseEntity.ok(materialMapper.toDto(materialService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) material")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) material")
    public ResponseEntity<MaterialDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                materialMapper.toDto(materialService.unarchiveById(id))
        );
    }

}
