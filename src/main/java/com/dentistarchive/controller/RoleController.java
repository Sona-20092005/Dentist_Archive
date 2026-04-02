package com.dentistarchive.controller;


import com.dentistarchive.dto.RoleDto;
import com.dentistarchive.mapper.RoleMapper;
import com.dentistarchive.search.filter.RoleFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.RoleSort;
import com.dentistarchive.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Roles")
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController extends BaseController {

    RoleService roleService;
    RoleMapper roleMapper;

    @GetMapping("/{id}")
    public RoleDto getRoleById(@PathVariable("id") UUID id) {
        return roleMapper.toDto(roleService.getByIdOrElseThrow(id));
    }

    @PostMapping("/search")
    public SearchResponse<RoleDto> searchRoles(@RequestBody SearchRequest<RoleFilter, RoleSort> searchRequest) {
        return roleMapper.toSearchResponse(roleService.search(searchRequest));
    }
}
