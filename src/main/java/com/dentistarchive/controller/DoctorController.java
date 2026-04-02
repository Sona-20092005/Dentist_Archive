package com.dentistarchive.controller;

import com.dentistarchive.dto.DoctorDto;
import com.dentistarchive.dto.auth.command.ChangePasswordCommand;
import com.dentistarchive.dto.auth.enums.UserScope;
import com.dentistarchive.dto.create.DoctorCreateDto;
import com.dentistarchive.mapper.DoctorMapper;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.search.filter.SearchRequest;
import com.dentistarchive.search.filter.SearchResponse;
import com.dentistarchive.search.sort.DoctorSort;
import com.dentistarchive.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorController extends BaseController {

    DoctorService userService;
    DoctorMapper userMapper;

    @GetMapping("/{id}")
    public DoctorDto getUserPublicInfoById(@PathVariable("id") UUID id) {
        return userMapper.toDto(userService.getByIdOrElseThrow(id));
    }

    @PostMapping("/search")
    public SearchResponse<DoctorDto> searchUsersPublicInfo(@RequestBody SearchRequest<DoctorFilter, DoctorSort> searchRequest) {
        return userMapper.toSearchResponse(userService.search(searchRequest));
    }

//    @GetMapping("/by-email-and-scope/exists")
//    public EntityExistsResponse userExistsByEmailAndScopeWithoutAccessControl(
//            @RequestParam("email") String email,
//            @RequestParam("scope") UserScope scope
//    ) {
//        boolean exists = SecurityManager.runWithoutAccessControl(() -> userService.exists(
//                new UserFilter()
//                        .setEmails(Set.of(email))
//                        .setScopes(Set.of(scope))
//        ));
//        return new EntityExistsResponse(exists);
////    }
//
//    @GetMapping("/by-phone-and-scope/exists")
//    public EntityExistsResponse userExistsByPhoneAndScopeWithoutAccessControl(
//            @RequestParam("phone") String phone,
//            @RequestParam("scope") ActorScope scope
//    ) {
//        boolean exists = SecurityManager.runWithoutAccessControl(() -> userService.exists(
//                new UserFilter()
//                        .setPhones(Set.of(phone))
//                        .setScopes(Set.of(scope))
//        ));
//        return new EntityExistsResponse(exists);
//    }

    @PostMapping("/doctor")
    @Operation(summary = "Register new usual user with email that was confirmed on previous step in the same session")
    public DoctorDto createUsualUser(@RequestBody DoctorCreateDto userCreateDto) {
        return userMapper.toDto(userService.createUsualUser(userCreateDto, true));
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Change user's password using old password or otp. Also supports first login with temporary password.")
    public void changeUserPassword(@RequestBody ChangePasswordCommand command) {
        userService.changeUserPassword(command);
    }

}
