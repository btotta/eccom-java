package com.tota.eccom.adapters.dto.user.response;

import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserRespDTO {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static UserRespDTO fromUser(User user) {

        return UserRespDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
