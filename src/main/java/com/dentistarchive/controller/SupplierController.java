package com.dentistarchive.controller;

import com.dentistarchive.dto.SupplierDto;
import com.dentistarchive.dto.create.SupplierCreateDto;
import com.dentistarchive.dto.update.SupplierUpdateDto;
import com.dentistarchive.mapper.SupplierMapper;
import com.dentistarchive.search.SupplierSort;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.SupplierFilter;
import com.dentistarchive.service.SupplierService;
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
@Tag(name = "Suppliers")
@RequestMapping("/api/v1/suppliers")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController extends BaseController {

    SupplierService supplierService;
    SupplierMapper supplierMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by id")
    public ResponseEntity<SupplierDto> getSupplierId(@PathVariable UUID id) {
        return ResponseEntity.ok(supplierMapper.toDto(supplierService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search supplier")
    public SearchResponse<SupplierDto> search(@RequestBody SearchRequest<SupplierFilter, SupplierSort> searchRequest) {
        return supplierMapper.toSearchResponse(supplierService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create supplier")
    public ResponseEntity<SupplierDto> create(@Valid @RequestBody SupplierCreateDto createDto) {
        return ResponseEntity.ok(supplierMapper.toDto(supplierService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update supplier")
    public ResponseEntity<SupplierDto> update(@PathVariable UUID id,
                                            @Valid @RequestBody SupplierUpdateDto updateDto) {
        return ResponseEntity.ok(supplierMapper.toDto(supplierService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) supplier")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        supplierService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) supplier")
    public ResponseEntity<SupplierDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                supplierMapper.toDto(supplierService.unarchiveById(id))
        );
    }

}
