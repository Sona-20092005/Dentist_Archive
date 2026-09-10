package com.dentistarchive.controller;

import com.dentistarchive.dto.TechnicianProcedureDto;
import com.dentistarchive.dto.create.TechnicianProcedureCreateDto;
import com.dentistarchive.dto.update.TechnicianProcedureUpdateDto;
import com.dentistarchive.mapper.TechnicianProcedureMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TechnicianProcedureFilter;
import com.dentistarchive.search.sort.TechnicianProcedureSort;
import com.dentistarchive.service.TechnicianProcedureService;
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
@Tag(name = "Technician Procedure")
@RequestMapping("/api/v1/technician-procedure")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianProcedureController extends BaseController {

    TechnicianProcedureService technicianProcedureService;
    TechnicianProcedureMapper technicianProcedureMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get procedure by id")
    public ResponseEntity<TechnicianProcedureDto> getTechnicianProcedureById(@PathVariable UUID id) {
        return ResponseEntity.ok(technicianProcedureMapper.toDto(technicianProcedureService.getByIdOrElseThrow(id)));
    }


    @PostMapping("/search")
    @Operation(summary = "Search technician procedure")
    public SearchResponse<TechnicianProcedureDto> search(@RequestBody SearchRequest<TechnicianProcedureFilter, TechnicianProcedureSort> searchRequest) {
        return technicianProcedureMapper.toSearchResponse(technicianProcedureService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create technician procedure")
    public ResponseEntity<TechnicianProcedureDto> create(@Valid @RequestBody TechnicianProcedureCreateDto createDto) {
        return ResponseEntity.ok(technicianProcedureMapper.toDto(technicianProcedureService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update technician procedure info")
    public ResponseEntity<TechnicianProcedureDto> update(@PathVariable UUID id,
                                                         @Valid @RequestBody TechnicianProcedureUpdateDto updateDto) {
        return ResponseEntity.ok(technicianProcedureMapper.toDto(technicianProcedureService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) technician procedure")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        technicianProcedureService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) technician procedure")
    public ResponseEntity<TechnicianProcedureDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                technicianProcedureMapper.toDto(technicianProcedureService.unarchiveById(id))
        );
    }

}
