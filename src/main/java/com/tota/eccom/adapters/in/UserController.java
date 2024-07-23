package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
import com.tota.eccom.adapters.dto.user.response.UserRespDTO;
import com.tota.eccom.domain.user.IUserService;
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

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {

    private final IUserService userDomain;

    // User Operations

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRespDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return new ResponseEntity<>(new UserRespDTO(userDomain.createUser(userCreateDTO)), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get logged user", description = "Fetches the details of the logged-in user.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserRespDTO> getUserLogged() {
        return new ResponseEntity<>(new UserRespDTO(userDomain.getUserLogged()), HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update logged user", description = "Updates the details of the logged-in user.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> updateUserLogged(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(new UserRespDTO(userDomain.updateUserLogged(userUpdateDTO)), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Delete logged user", description = "Deletes the logged-in user's account.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Void> deleteUserLogged() {
        userDomain.deleteUserLogged();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // User by Admin Operations

    @GetMapping("/admin/{id}")
    @Operation(summary = "Get user by id", description = "Fetches the details of a user by their ID.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> getUserById(@Parameter(description = "ID of the user to be fetched") @PathVariable Long id) {
        return new ResponseEntity<>(new UserRespDTO(userDomain.getUserById(id)), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @Operation(summary = "Update user by id", description = "Updates the details of a user by their ID.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<UserRespDTO> updateUserById(@Parameter(description = "ID of the user to be updated") @PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        return new ResponseEntity<>(new UserRespDTO(userDomain.updateUserById(id, userUpdateDTO)), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Delete user by id", description = "Deletes a user by their ID.", security = @SecurityRequirement(name = "Authorization"))
    @PreAuthorize("hasRole('ADMIN')")
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

    // Login and Logout Operations

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Logs in the user with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserLoginRespDTO> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return new ResponseEntity<>(userDomain.loginUser(userLoginDTO), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out the user.", security = @SecurityRequirement(name = "Authorization"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> logoutUser() {
        userDomain.logoutUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
