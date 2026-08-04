package com.dentistarchive.controller;

import com.dentistarchive.dto.NurseWorkLogDto;
import com.dentistarchive.dto.create.NurseWorkLogCreateDto;
import com.dentistarchive.dto.update.NurseWorkLogUpdateDto;
import com.dentistarchive.mapper.NurseWorkLogMapper;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.NurseWorkLogSort;
import com.dentistarchive.service.NurseWorkLogService;
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
@Tag(name = "Nurse Work Log")
@RequestMapping("/api/v1/nurse-work-logs")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseWorkLogController extends BaseController {

    NurseWorkLogService nurseWorkLogService;
    NurseWorkLogMapper nurseWorkLogMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get nurse work log by id")
    public ResponseEntity<NurseWorkLogDto> getNurseById(@PathVariable UUID id) {
        return ResponseEntity.ok(nurseWorkLogMapper.toDto(nurseWorkLogService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search nurse work log")
    public SearchResponse<NurseWorkLogDto> search(@RequestBody SearchRequest<NurseWorkLogFilter, NurseWorkLogSort> searchRequest) {
        return nurseWorkLogMapper.toSearchResponse(nurseWorkLogService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create nurse work log")
    public ResponseEntity<NurseWorkLogDto> create(@Valid @RequestBody NurseWorkLogCreateDto createDto) {
        return ResponseEntity.ok(nurseWorkLogMapper.toDto(nurseWorkLogService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update nurse work log info")
    public ResponseEntity<NurseWorkLogDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody NurseWorkLogUpdateDto updateDto) {
        return ResponseEntity.ok(nurseWorkLogMapper.toDto(nurseWorkLogService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) nurse work log")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        nurseWorkLogService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) nurse work log")
    public ResponseEntity<NurseWorkLogDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                nurseWorkLogMapper.toDto(nurseWorkLogService.unarchiveById(id))
        );
    }

}

