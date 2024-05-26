package com.tota.eccom.adapters.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefreshRequest {

    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;
}
