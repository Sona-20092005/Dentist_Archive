package com.dentistarchive.controller;

import com.dentistarchive.dto.create.ClinicRentAgreementCreateDto;
import com.dentistarchive.dto.ClinicRentAgreementDto;
import com.dentistarchive.dto.update.ClinicRentAgreementUpdateDto;
import com.dentistarchive.mapper.ClinicRentAgreementMapper;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.ClinicRentAgreementSort;
import com.dentistarchive.service.ClinicRentAgreementService;
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
@Tag(name = "Clinic Rent Agreement")
@RequestMapping("/api/v1/clinics-rent-agreements")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicRentAgreementController extends BaseController {

    ClinicRentAgreementService clinicRentAgreementService;
    ClinicRentAgreementMapper clinicRentAgreementMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get clinic rent agreement by id")
    public ResponseEntity<ClinicRentAgreementDto> getClinicRentAgreementById(@PathVariable UUID id) {
        return ResponseEntity.ok(clinicRentAgreementMapper.toDto(clinicRentAgreementService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search clinic rent agreements")
    public SearchResponse<ClinicRentAgreementDto> search(@RequestBody SearchRequest<ClinicRentAgreementFilter, ClinicRentAgreementSort> searchRequest) {
        return clinicRentAgreementMapper.toSearchResponse(clinicRentAgreementService.search(searchRequest));
    }

    @PostMapping
    @Operation(summary = "Create clinic rent agreement")
    public ResponseEntity<ClinicRentAgreementDto> create(@Valid @RequestBody ClinicRentAgreementCreateDto createDto) {
        return ResponseEntity.ok(clinicRentAgreementMapper.toDto(clinicRentAgreementService.create(createDto)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update clinic rent agreement")
    public ResponseEntity<ClinicRentAgreementDto> update(@PathVariable UUID id,
                                                          @Valid @RequestBody ClinicRentAgreementUpdateDto updateDto) {
        return ResponseEntity.ok(clinicRentAgreementMapper.toDto(clinicRentAgreementService.update(id, updateDto)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (archive) clinic rent agreement")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clinicRentAgreementService.archiveById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore (unarchive) clinic rent agreement")
    public ResponseEntity<ClinicRentAgreementDto> unarchive(@PathVariable UUID id) {
        return ResponseEntity.ok(
                clinicRentAgreementMapper.toDto(clinicRentAgreementService.unarchiveById(id))
        );
    }

}
