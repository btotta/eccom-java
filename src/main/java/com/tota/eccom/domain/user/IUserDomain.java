package com.tota.eccom.domain.user;

import com.tota.eccom.adapters.dto.user.UserCreate;
import com.tota.eccom.adapters.dto.user.UserUpdate;
import com.tota.eccom.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public interface IUserDomain {

    User createUser(UserCreate userCreateDTO);

    User getUserById(Long id);

    void deleteUserById(Long id);

    User updateUserById(Long id, UserUpdate userUpdateDTO);

    User getUserLogged();

    void deleteUserLogged();

    User updateUserLogged(UserUpdate userUpdateDTO);
}
