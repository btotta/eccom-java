package com.tota.eccom.adapters.dto.user.response;

import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserRespDTO {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private Status status;
    private Date createdAt;
    private Date updatedAt;


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
