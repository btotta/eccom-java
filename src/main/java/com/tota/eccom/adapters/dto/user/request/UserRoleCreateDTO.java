package com.tota.eccom.adapters.dto.user.request;

import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import lombok.Data;

@Data
public class UserRoleCreateDTO {

    private String name;
    private Status status;

    public Role toUserRole() {
        return Role.builder()
                .name(name)
                .status(status)
                .build();
    }
}
