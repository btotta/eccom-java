package com.tota.eccom.adapters.dto.user.response;

import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private Status status;
    private Date createdAt;
    private Date updatedAt;


    public UserRespDTO(User user) {

        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();

    }

}
