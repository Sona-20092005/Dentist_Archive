package com.dentistarchive.controller;

import com.dentistarchive.dto.NurseDto;
import com.dentistarchive.dto.create.NurseCreateDto;
import com.dentistarchive.dto.update.NurseUpdateDto;
import com.dentistarchive.mapper.NurseMapper;
import com.dentistarchive.search.filter.NurseFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.NurseSort;
import com.dentistarchive.service.NurseService;
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
@Tag(name = "Nurse")
@RequestMapping("/api/v1/nurses")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseController extends BaseController {

    NurseService nurseService;
    NurseMapper nurseMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get nurse by id")
    public ResponseEntity<NurseDto> getNurseById(@PathVariable UUID id) {
        return ResponseEntity.ok(nurseMapper.toDto(nurseService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search nurse")
    public SearchResponse<NurseDto> search(@RequestBody SearchRequest<NurseFilter, NurseSort> searchRequest) {
        return nurseMapper.toSearchResponse(nurseService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create nurse")
    public ResponseEntity<NurseDto> create(@Valid @RequestBody NurseCreateDto createDto) {
        return ResponseEntity.ok(nurseMapper.toDto(nurseService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update nurse info")
    public ResponseEntity<NurseDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody NurseUpdateDto updateDto) {
        return ResponseEntity.ok(nurseMapper.toDto(nurseService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) nurse")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        nurseService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) nurse")
    public ResponseEntity<NurseDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                nurseMapper.toDto(nurseService.unarchiveById(id))
        );
    }

}

