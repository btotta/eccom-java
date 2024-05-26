package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.exception.ErrorDetails;
import com.tota.eccom.adapters.dto.user.UserCreate;
import com.tota.eccom.adapters.dto.user.UserUpdate;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final IUserDomain userDomain;

    // Public routes
    @PostMapping("/public")
    @Operation(summary = "Create a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreate userCreateDTO) {
        return new ResponseEntity<>(userDomain.createUser(userCreateDTO), HttpStatus.CREATED);
    }

    // Private routes for logged user
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get logged user", description = "Fetches the details of the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<User> getUserById() {
        return new ResponseEntity<>(userDomain.getUserLogged(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    @Operation(summary = "Delete logged user", description = "Deletes the logged-in user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
    })
    public ResponseEntity<Void> deleteUserById() {
        userDomain.deleteUserLogged();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    @Operation(summary = "Update logged user", description = "Updates the details of the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
    })
    public ResponseEntity<User> updateUserById(@RequestBody @Valid UserUpdate userUpdateDTO) {
        return new ResponseEntity<>(userDomain.updateUserLogged(userUpdateDTO), HttpStatus.OK);
    }

    // Private routes for admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    @Operation(summary = "Get user by id", description = "Fetches the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user details", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
    })
    public ResponseEntity<User> getUserById(@Parameter(description = "ID of the user to be fetched") @PathVariable Long id) {
        return new ResponseEntity<>(userDomain.getUserById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Delete user by id", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
    })
    public ResponseEntity<Void> deleteUserById(@Parameter(description = "ID of the user to be deleted") @PathVariable Long id) {
        userDomain.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    @Operation(summary = "Update user by id", description = "Updates the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorDetails.class))),
    })
    public ResponseEntity<User> updateUserById(@Parameter(description = "ID of the user to be updated") @PathVariable Long id, @RequestBody @Valid UserUpdate userUpdateDTO) {
        return new ResponseEntity<>(userDomain.updateUserById(id, userUpdateDTO), HttpStatus.OK);
    }
}
