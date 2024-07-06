package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserRespDTO;
import com.tota.eccom.adapters.dto.user.response.UserRoleRespDTO;
import com.tota.eccom.domain.user.IUserDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {

    private final IUserDomain userDomain;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRespDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.createUser(userCreateDTO)), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get logged user", description = "Fetches the details of the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRespDTO> getUserLogged() {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.getUserLogged()), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Delete logged user", description = "Deletes the logged-in user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteUserLogged() {
        userDomain.deleteUserLogged();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(summary = "Update logged user", description = "Updates the details of the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> updateUserLogged(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.updateUserLogged(userUpdateDTO)), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    @Operation(summary = "Get user by id", description = "Fetches the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> getUserById(@Parameter(description = "ID of the user to be fetched") @PathVariable Long id) {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.getUserById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Delete user by id", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteUserById(@Parameter(description = "ID of the user to be deleted") @PathVariable Long id) {
        userDomain.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/admin/{id}")
    @Operation(summary = "Update user by id", description = "Updates the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> updateUserById(@Parameter(description = "ID of the user to be updated") @PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.updateUserById(id, userUpdateDTO)), HttpStatus.OK);
    }


    // User role routes
    @GetMapping("/role/list")
    @Operation(summary = "Get all user roles", description = "Fetches all user roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user roles"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserRoleRespDTO>> getAllUserRoles() {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRoles(userDomain.getAllUserRoles()), HttpStatus.OK);
    }

    @PostMapping("/role")
    @Operation(summary = "Create a new user role", description = "Creates a new user role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRoleRespDTO> createUserRole(@RequestBody @Valid UserRoleCreateDTO userRoleCreateDTO) {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRole(userDomain.createUserRole(userRoleCreateDTO)), HttpStatus.CREATED);
    }

    @DeleteMapping("/role/{id}")
    @Operation(summary = "Delete user role by id", description = "Deletes a user role by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User role deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteUserRoleById(@Parameter(description = "ID of the user role to be deleted") @PathVariable Long id) {
        userDomain.deleteUserRoleById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/role/{id}")
    @Operation(summary = "Get user role by id", description = "Fetches the details of a user role by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user role details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRoleRespDTO> getUserRoleById(@Parameter(description = "ID of the user role to be fetched") @PathVariable Long id) {
        return new ResponseEntity<>(UserRoleRespDTO.fromUserRole(userDomain.getUserRoleById(id)), HttpStatus.OK);
    }


    @PostMapping("/role/{id}/user/{userId}")
    @Operation(summary = "Associate user role to user", description = "Associates a user role to a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User role associated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User role or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<UserRespDTO> associateUserRoleToUser(@Parameter(description = "ID of the user role to be associated") @PathVariable Long id, @Parameter(description = "ID of the user to be associated") @PathVariable Long userId) {
        return new ResponseEntity<>(UserRespDTO.fromUser(userDomain.associateUserRole(userId, id)), HttpStatus.CREATED);
    }
}
