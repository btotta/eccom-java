package com.tota.eccom.adapters.dto.user.response;

import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.model.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleRespDTO {

    private Long id;
    private String name;
    private Status status;

    public UserRoleRespDTO(UserRole userRole) {
        this.id = userRole.getId();
        this.name = userRole.getName();
        this.status = userRole.getStatus();
    }

    public static List<UserRoleRespDTO> fromUserRoles(List<UserRole> userRoles) {
        return userRoles.stream().map(UserRoleRespDTO::new).toList();
    }

    public static UserRoleRespDTO fromUserRole(UserRole userRole) {
        return new UserRoleRespDTO(userRole);
    }
}
