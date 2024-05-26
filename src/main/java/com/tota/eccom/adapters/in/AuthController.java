package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.auth.LoginRequest;
import com.tota.eccom.adapters.dto.auth.LoginResponse;
import com.tota.eccom.adapters.dto.auth.RefreshRequest;
import com.tota.eccom.domain.auth.IAuthDomain;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthDomain authDomain;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authDomain.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return new ResponseEntity<>(authDomain.refresh(refreshRequest), HttpStatus.OK);
    }
}
