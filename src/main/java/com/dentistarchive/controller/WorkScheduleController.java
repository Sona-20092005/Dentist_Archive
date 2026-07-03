package com.dentistarchive.controller;

import com.dentistarchive.dto.WorkScheduleDto;
import com.dentistarchive.dto.create.WorkScheduleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRevisionCommand;
import com.dentistarchive.dto.update.WorkScheduleUpdateDto;
import com.dentistarchive.mapper.WorkScheduleMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.WorkScheduleFilter;
import com.dentistarchive.search.sort.WorkScheduleSort;
import com.dentistarchive.service.WorkScheduleService;
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
@Tag(name = "Work Schedule")
@RequestMapping("/api/v1/work-schedules")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleController extends BaseController {

    WorkScheduleService scheduleService;
    WorkScheduleMapper scheduleMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get work schedule by id")
    public ResponseEntity<WorkScheduleDto> getWorkScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search work schedule")
    public SearchResponse<WorkScheduleDto> search(@RequestBody SearchRequest<WorkScheduleFilter, WorkScheduleSort> searchRequest) {
        return scheduleMapper.toSearchResponse(scheduleService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create work schedule")
    public ResponseEntity<WorkScheduleDto> create(@Valid @RequestBody WorkScheduleCreateDto createDto) {
        return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update work schedule info")
    public ResponseEntity<WorkScheduleDto> update(@PathVariable UUID id,
                                             @Valid @RequestBody WorkScheduleUpdateDto updateDto) {
        return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.update(id, updateDto)));
    }

    @PostMapping("/revise/{id}")
    @Operation(summary = "Revise work schedule")
    public ResponseEntity<WorkScheduleDto> revise(@PathVariable UUID id, @RequestBody @Valid WorkScheduleRevisionCommand dto) {
        return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.revise(id, dto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) work schedule")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) work schedule")
    public ResponseEntity<WorkScheduleDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                scheduleMapper.toDto(scheduleService.unarchiveById(id))
        );
    }

}
