package com.tota.eccom.domain.user;

import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IRoleService {
    List<Role> getAllUserRoles();

    Role createUserRole(UserRoleCreateDTO userRoleCreateDTO);

    Role getUserRoleById(Long id);

    User associateUserRole(Long userId, Long id);

    void deleteUserRoleById(Long id);
}
