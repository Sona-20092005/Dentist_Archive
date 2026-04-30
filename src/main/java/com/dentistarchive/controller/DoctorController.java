package com.dentistarchive.controller;

import com.dentistarchive.dto.DoctorDto;
import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.dto.update.DoctorUpdateDto;
import com.dentistarchive.mapper.DoctorMapper;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.DoctorSort;
import com.dentistarchive.service.DoctorService;
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
@Tag(name = "Doctors")
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorController extends BaseController {

    DoctorService doctorService;
    DoctorMapper doctorMapper;

    @GetMapping("/{id}")
    public DoctorDto getById(@PathVariable("id") UUID id) {
        return doctorMapper.toDto(doctorService.getByIdOrElseThrow(id));
    }

    @PostMapping("/search")
    public SearchResponse<DoctorDto> search(@RequestBody SearchRequest<DoctorFilter, DoctorSort> searchRequest) {
        return doctorMapper.toSearchResponse(doctorService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Register new doctor")
    public DoctorDto create(@RequestBody DoctorCreateDto doctorCreateDto) {
        return doctorMapper.toDto(doctorService.create(doctorCreateDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor info")
    public DoctorDto update(@PathVariable UUID id,
                            @Valid @RequestBody DoctorUpdateDto updateDto) {
        return doctorMapper.toDto(doctorService.update(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<DoctorDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                doctorMapper.toDto(doctorService.unarchiveById(id))
        );
    }

}
