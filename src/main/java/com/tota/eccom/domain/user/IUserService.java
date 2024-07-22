package com.tota.eccom.domain.user;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public interface IUserService {

    User createUser(UserCreateDTO userCreateDTO);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User updateUserById(Long id, UserUpdateDTO userUpdateDTO);

    User getUserLogged();

    void deleteUserLogged();

    User updateUserLogged(UserUpdateDTO userUpdateDTO);

    Role getUserRole();

    Role getAdminRole();

    UserLoginRespDTO loginUser(UserLoginDTO userLoginDTO);

    void logoutUser();
}
