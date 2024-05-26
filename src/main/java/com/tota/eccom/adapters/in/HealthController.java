package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.health.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Hello World")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check")
    public ResponseEntity<HealthResponse> healthCheck() {

        return new ResponseEntity<>(HealthResponse.builder()
                .status("UP")
                .message("Service is running")
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.OK);
    }
}
