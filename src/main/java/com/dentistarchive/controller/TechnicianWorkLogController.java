package com.dentistarchive.controller;

import com.dentistarchive.dto.TechnicianWorkLogDto;
import com.dentistarchive.dto.create.TechnicianWorkLogCreateDto;
import com.dentistarchive.dto.update.TechnicianWorkLogUpdateDto;
import com.dentistarchive.mapper.TechnicianWorkLogMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.TechnicianWorkLogFilter;
import com.dentistarchive.search.sort.TechnicianWorkLogSort;
import com.dentistarchive.service.TechnicianWorkLogService;
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
@Tag(name = "Technician Work Log")
@RequestMapping("/api/v1/technician-work-logs")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianWorkLogController extends BaseController {

    TechnicianWorkLogService technicianWorkLogService;
    TechnicianWorkLogMapper technicianWorkLogMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get technician work log by id")
    public ResponseEntity<TechnicianWorkLogDto> getTechnicianWorkLogById(@PathVariable UUID id) {
        return ResponseEntity.ok(technicianWorkLogMapper.toDto(technicianWorkLogService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search technician work logs")
    public SearchResponse<TechnicianWorkLogDto> search(@RequestBody SearchRequest<TechnicianWorkLogFilter, TechnicianWorkLogSort> searchRequest) {
        return technicianWorkLogMapper.toSearchResponse(technicianWorkLogService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create technician work log")
    public ResponseEntity<TechnicianWorkLogDto> create(@Valid @RequestBody TechnicianWorkLogCreateDto createDto) {
        return ResponseEntity.ok(technicianWorkLogMapper.toDto(technicianWorkLogService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update technician work log info")
    public ResponseEntity<TechnicianWorkLogDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody TechnicianWorkLogUpdateDto updateDto) {
        return ResponseEntity.ok(technicianWorkLogMapper.toDto(technicianWorkLogService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) technician work log")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        technicianWorkLogService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) technician work log")
    public ResponseEntity<TechnicianWorkLogDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                technicianWorkLogMapper.toDto(technicianWorkLogService.unarchiveById(id))
        );
    }

}
