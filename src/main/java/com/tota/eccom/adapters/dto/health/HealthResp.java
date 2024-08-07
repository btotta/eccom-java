package com.tota.eccom.adapters.dto.health;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthResp {

    private String status;
    private String message;
    private LocalDateTime timestamp;
}
