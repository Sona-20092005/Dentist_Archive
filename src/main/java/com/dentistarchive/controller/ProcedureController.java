package com.dentistarchive.controller;

import com.dentistarchive.dto.ProcedureDto;
import com.dentistarchive.dto.create.ProcedureCreateDto;
import com.dentistarchive.dto.update.ProcedureUpdateDto;
import com.dentistarchive.mapper.ProcedureMapper;
import com.dentistarchive.search.filter.ProcedureFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.ProcedureSort;
import com.dentistarchive.service.ProcedureService;
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
@Tag(name = "Procedure")
@RequestMapping("/api/v1/procedure")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcedureController extends BaseController {

    ProcedureService procedureService;
    ProcedureMapper procedureMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get procedure by id")
    public ResponseEntity<ProcedureDto> getProcedureById(@PathVariable UUID id) {
        return ResponseEntity.ok(procedureMapper.toDto(procedureService.getByIdOrElseThrow(id)));
    }


    @PostMapping("/search")
    @Operation(summary = "Search procedure")
    public SearchResponse<ProcedureDto> search(@RequestBody SearchRequest<ProcedureFilter, ProcedureSort> searchRequest) {
        return procedureMapper.toSearchResponse(procedureService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create procedure")
    public ResponseEntity<ProcedureDto> create(@Valid @RequestBody ProcedureCreateDto createDto) {
        return ResponseEntity.ok(procedureMapper.toDto(procedureService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update procedure info")
    public ResponseEntity<ProcedureDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody ProcedureUpdateDto updateDto) {
        return ResponseEntity.ok(procedureMapper.toDto(procedureService.update(id, updateDto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) procedure")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        procedureService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) procedure")
    public ResponseEntity<ProcedureDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                procedureMapper.toDto(procedureService.unarchiveById(id))
        );
    }

}
