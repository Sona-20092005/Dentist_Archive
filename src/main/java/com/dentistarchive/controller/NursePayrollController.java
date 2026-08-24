package com.dentistarchive.controller;

import com.dentistarchive.dto.NursePayrollDto;
import com.dentistarchive.dto.update.NursePayrollUpdateDto;
import com.dentistarchive.mapper.NursePayrollMapper;
import com.dentistarchive.search.filter.NursePayrollFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.NursePayrollSort;
import com.dentistarchive.service.NursePayrollService;
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
@Tag(name = "Nurse Payroll")
@RequestMapping("/api/v1/nurse-payroll")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NursePayrollController extends BaseController {

    NursePayrollService nursePayrollService;
    NursePayrollMapper nursePayrollMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get nurse payroll by id")
    public ResponseEntity<NursePayrollDto> getNurseById(@PathVariable UUID id) {
        return ResponseEntity.ok(nursePayrollMapper.toDto(nursePayrollService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search nurse payrolls")
    public SearchResponse<NursePayrollDto> search(@RequestBody SearchRequest<NursePayrollFilter, NursePayrollSort> searchRequest) {
        return nursePayrollMapper.toSearchResponse(nursePayrollService.search(searchRequest));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update nurse payroll info")
    public ResponseEntity<NursePayrollDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody NursePayrollUpdateDto updateDto) {
        return ResponseEntity.ok(nursePayrollMapper.toDto(nursePayrollService.update(id, updateDto)));
    }


}

