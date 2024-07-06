package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.health.HealthResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping
@Tag(name = "Health", description = "Endpoints for health check")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Hello World")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello World", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is running", content = @Content(schema = @Schema(implementation = HealthResp.class)))
    })
    public ResponseEntity<HealthResp> healthCheck() {

        return new ResponseEntity<>(HealthResp.builder()
                .status("UP")
                .message("Service is running")
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.OK);
    }
}
