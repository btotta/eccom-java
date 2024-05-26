package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.user.UserCreate;
import com.tota.eccom.adapters.dto.user.UserUpdate;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final IUserDomain userDomain;


    @PostMapping("/public/user")
    @Operation(summary = "Create a new user")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreate userCreateDTO) {
        return new ResponseEntity<>(userDomain.createUser(userCreateDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    @Operation(summary = "Get logged user")
    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userDomain.getUserLogged(token), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/user")
    @Operation(summary = "Delete logged user")
    public ResponseEntity<Void> deleteUserById(@RequestHeader("Authorization") String token) {
        userDomain.deleteUserLogged(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/user")
    @Operation(summary = "Update logged user")
    public ResponseEntity<User> updateUserById(@RequestHeader("Authorization") String token, @RequestBody @Valid UserUpdate userUpdateDTO) {
        return new ResponseEntity<>(userDomain.updateUserLogged(token, userUpdateDTO), HttpStatus.OK);
    }

}
