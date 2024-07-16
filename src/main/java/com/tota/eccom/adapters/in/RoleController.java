package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.adapters.dto.user.response.UserRespDTO;
import com.tota.eccom.adapters.dto.user.response.UserRoleRespDTO;
import com.tota.eccom.domain.user.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Endpoints for role management")
public class RoleController {


    private final IRoleService roleService;


    @GetMapping("/list")
    @Operation(summary = "Get all user roles", description = "Fetches all user roles.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user roles"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserRoleRespDTO>> getAllUserRoles() {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRoles(roleService.getAllUserRoles()), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "Create a new user role", description = "Creates a new user role.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRoleRespDTO> createUserRole(@RequestBody @Valid UserRoleCreateDTO userRoleCreateDTO) {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRole(roleService.createUserRole(userRoleCreateDTO)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user role by id", description = "Fetches the details of a user role by their ID.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user role details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRoleRespDTO> getUserRoleById(@Parameter(description = "ID of the user role to be fetched") @PathVariable Long id) {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRole(roleService.getUserRoleById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user role by id", description = "Deletes a user role by their ID.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User role deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteUserRoleById(@Parameter(description = "ID of the user role to be deleted") @PathVariable Long id) {
        roleService.deleteUserRoleById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/user/{userId}")
    @Operation(summary = "Associate user role to user", description = "Associates a user role to a user.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User role associated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<UserRespDTO> associateUserRoleToUser(@Parameter(description = "ID of the user role to be associated") @PathVariable Long id, @Parameter(description = "ID of the user to be associated") @PathVariable Long userId) {
        return new ResponseEntity<>(UserRespDTO.fromUser(roleService.associateUserRole(userId, id)), HttpStatus.CREATED);
    }

}
