package com.tota.eccom.domain.user;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IUserDomain {

    User createUser(UserCreateDTO userCreateDTO);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User updateUserById(Long id, UserUpdateDTO userUpdateDTO);

    User getUserLogged();

    void deleteUserLogged();

    User updateUserLogged(UserUpdateDTO userUpdateDTO);

    List<Role> getAllUserRoles();

    Role createUserRole(UserRoleCreateDTO userRoleCreateDTO);

    void deleteUserRoleById(Long id);

    Role getUserRoleById(Long id);

    User associateUserRole(Long userId, Long id);

    UserLoginRespDTO loginUser(UserLoginDTO userLoginDTO);

    void logoutUser(String authorization);
}
