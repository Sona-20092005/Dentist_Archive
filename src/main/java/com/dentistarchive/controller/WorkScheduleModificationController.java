package com.dentistarchive.controller;

import com.dentistarchive.dto.WorkScheduleModificationDto;
import com.dentistarchive.dto.create.WorkScheduleModificationCreateDto;
import com.dentistarchive.mapper.WorkScheduleModificationMapper;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.search.sort.WorkScheduleModificationSort;
import com.dentistarchive.service.WorkScheduleModificationService;
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
@Tag(name = "Work Schedule Modification")
@RequestMapping("/api/v1/work-schedules-modification")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleModificationController extends BaseController {

    WorkScheduleModificationService scheduleModificationService;
    WorkScheduleModificationMapper scheduleModificationMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get work schedule modification by id")
    public ResponseEntity<WorkScheduleModificationDto> getWorkScheduleModificationById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleModificationMapper.toDto(scheduleModificationService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search work schedule modification")
    public SearchResponse<WorkScheduleModificationDto> search(@RequestBody SearchRequest<WorkScheduleModificationFilter, WorkScheduleModificationSort> searchRequest) {
        return scheduleModificationMapper.toSearchResponse(scheduleModificationService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create work schedule modification")
    public ResponseEntity<WorkScheduleModificationDto> create(@Valid @RequestBody WorkScheduleModificationCreateDto createDto) {
        return ResponseEntity.ok(scheduleModificationMapper.toDto(scheduleModificationService.create(createDto)));
    }

//    @PatchMapping("/{id}")
//    @Operation(summary = "Update work schedule modification info")
//    public ResponseEntity<WorkScheduleModificationDto> update(@PathVariable UUID id,
//                                             @Valid @RequestBody WorkScheduleModificationUpdateDto updateDto) {
//        return ResponseEntity.ok(scheduleModificationMapper.toDto(scheduleModificationService.update(id, updateDto)));
//    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) work schedule modification")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        scheduleModificationService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) work schedule modification")
    public ResponseEntity<WorkScheduleModificationDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                scheduleModificationMapper.toDto(scheduleModificationService.unarchiveById(id))
        );
    }

}
