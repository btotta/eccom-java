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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserDomain userDomain;


    @PostMapping()
    @Operation(summary = "Create a new user")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreate userCreateDTO) {
        return new ResponseEntity<>(userDomain.createUser(userCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userDomain.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userDomain.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdate userUpdateDTO) {
        return new ResponseEntity<>(userDomain.updateUserById(id, userUpdateDTO), HttpStatus.OK);
    }

}
