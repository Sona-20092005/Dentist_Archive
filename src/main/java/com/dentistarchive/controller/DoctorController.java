package com.dentistarchive.controller;

import com.dentistarchive.dto.DoctorDto;
import com.dentistarchive.mapper.DoctorMapper;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.DoctorSort;
import com.dentistarchive.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorController extends BaseController {

    DoctorService doctorService;
    DoctorMapper doctorMapper;

    @GetMapping("/{id}")
    public DoctorDto getUserPublicInfoById(@PathVariable("id") UUID id) {
        return doctorMapper.toDto(doctorService.getByIdOrElseThrow(id));
    }

    @PostMapping("/search")
    public SearchResponse<DoctorDto> searchUsersPublicInfo(@RequestBody SearchRequest<DoctorFilter, DoctorSort> searchRequest) {
        return doctorMapper.toSearchResponse(doctorService.search(searchRequest));
    }

//    @PostMapping("/doctor")
//    @Operation(summary = "Register new usual user with email that was confirmed on previous step in the same session")
//    public DoctorDto createUsualUser(@RequestBody DoctorCreateDto userCreateDto) {
//        return doctorMapper.toDto(doctorService.createUsualUser(userCreateDto, true));
//    }

}
