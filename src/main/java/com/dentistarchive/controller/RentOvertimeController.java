package com.dentistarchive.controller;

import com.dentistarchive.dto.RentOvertimeDto;
import com.dentistarchive.dto.update.RentOvertimeUpdateDto;
import com.dentistarchive.mapper.RentOvertimeMapper;
import com.dentistarchive.search.filter.RentOvertimeFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.RentOvertimeSort;
import com.dentistarchive.service.RentOvertimeService;
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
@Tag(name = "Rent Overtime")
@RequestMapping("/api/v1/rent-overtimes")
@PreAuthorize("hasRole('DOCTOR')")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentOvertimeController extends BaseController {

    RentOvertimeService rentOvertimeService;
    RentOvertimeMapper rentOvertimeMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get rent overtime by id")
    public ResponseEntity<RentOvertimeDto> getRentOvertime(@PathVariable UUID id) {
        return ResponseEntity.ok(rentOvertimeMapper.toDto(rentOvertimeService.getByIdOrElseThrow(id)));
    }

    @PostMapping("/search")
    @Operation(summary = "Search rent overtimes")
    public SearchResponse<RentOvertimeDto> search(@RequestBody SearchRequest<RentOvertimeFilter, RentOvertimeSort> searchRequest) {
        return rentOvertimeMapper.toSearchResponse(rentOvertimeService.search(searchRequest));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update rent overtime info")
    public ResponseEntity<RentOvertimeDto> update(@PathVariable UUID id,
                                           @Valid @RequestBody RentOvertimeUpdateDto updateDto) {
        return ResponseEntity.ok(rentOvertimeMapper.toDto(rentOvertimeService.update(id, updateDto)));
    }


}

