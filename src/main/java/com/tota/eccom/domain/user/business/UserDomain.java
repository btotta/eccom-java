package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.UserCreate;
import com.tota.eccom.adapters.dto.user.UserUpdate;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.security.JwtTokenUtil;
import com.tota.eccom.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDomain implements IUserDomain {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public User createUser(UserCreate userCreateDTO) {

        if (getUserByEmail(userCreateDTO.getEmail()) != null) {
            throw new IllegalArgumentException("User already exists with given email.");
        }

        User user = userCreateDTO.toUser();

        log.info("Creating user: {}", user);

        return userRepository.save(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Getting user by id: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with given id: {}" + id));
    }

    @Override
    public void deleteUserById(Long id) {
        User user = getUserById(id);

        log.info("Deleting user by id: {}", id);

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public User updateUserById(Long id, UserUpdate userUpdateDTO) {
        User user = getUserById(id);

        log.info("Updating user: {}", user);

        userUpdateDTO.toUpdateUser(user);

        return userRepository.save(user);
    }

    @Override
    public User getUserLogged() {

        return getUserByEmail(JwtUserDetailsService.getLoggedUserEmail());
    }

    @Override
    public void deleteUserLogged() {

        User user = getUserLogged();

        log.info("Deleting user: {}", user);

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public User updateUserLogged(UserUpdate userUpdateDTO) {

        User user = getUserLogged();

        log.info("Updating logged user: {}", user);

        userUpdateDTO.toUpdateUser(user);

        return userRepository.save(user);
    }
}
