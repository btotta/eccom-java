package com.tota.eccom.adapters.dto.user.request;

import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.model.UserRole;
import lombok.Data;

@Data
public class UserRoleCreateDTO {

    private String name;
    private Status status;

    public UserRole toUserRole() {
        return UserRole.builder()
                .name(name)
                .status(status)
                .build();
    }
}
