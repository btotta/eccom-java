package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.model.UserRole;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.domain.user.repository.UserRoleRepository;
import com.tota.eccom.exceptions.user.UserAlreadyHasRoleException;
import com.tota.eccom.exceptions.user.UserEmailExistsException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.exceptions.user.UserRoleNotFoundException;
import com.tota.eccom.security.jwt.JwtTokenUtil;
import com.tota.eccom.security.jwt.JwtUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDomain implements IUserDomain {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    @Override
    public User createUser(UserCreateDTO userCreateDTO) {

        if (getUserByEmail(userCreateDTO.getEmail()) != null) {
            throw new UserEmailExistsException("User already exists with given email.");
        }

        UserRole userRole = getUserRoleByName("USER");

        User user = userCreateDTO.toUser();
        user.getRoles().add(userRole);

        log.info("Creating user: {}", user);

        return userRepository.save(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with given id: {}" + id));
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("Deleting user by id: {}", id);

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUserById(Long id, UserUpdateDTO userUpdateDTO) {
        User user = getUserById(id);

        User existingUser = getUserByEmail(userUpdateDTO.getEmail());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new UserEmailExistsException("User already exists with given email.");
        }

        log.info("Updating user: {}", user);

        userUpdateDTO.toUpdateUser(user);

        return userRepository.save(user);
    }

    @Override
    public User getUserLogged() {

        return getUserByEmail(JwtUserDetailsService.getLoggedUserEmail());
    }

    @Transactional
    @Override
    public void deleteUserLogged() {

        User user = getUserLogged();

        log.info("Deleting user: {}", user);

        user.setStatus(Status.DELETED);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUserLogged(UserUpdateDTO userUpdateDTO) {

        User user = getUserLogged();

        log.info("Updating logged user: {}", user);

        userUpdateDTO.toUpdateUser(user);

        return userRepository.save(user);
    }

    @Override
    public List<UserRole> getAllUserRoles() {

        return userRoleRepository.findAllActive();
    }

    @Override
    public UserRole createUserRole(UserRoleCreateDTO userRoleCreateDTO) {
        UserRole userRole = userRoleCreateDTO.toUserRole();

        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole getUserRoleById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found with given id: {}" + id));
    }


    public UserRole getUserRoleByName(String name) {
        return userRoleRepository.findByName(name)
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found with given name: {}" + name));
    }

    @Transactional
    @Override
    public void deleteUserRoleById(Long id) {
        UserRole userRole = getUserRoleById(id);

        userRoleRepository.delete(userRole);
    }

    @Transactional
    @Override
    public User associateUserRole(Long userId, Long roleId) {
        User user = getUserById(userId);
        UserRole userRole = getUserRoleById(roleId);

        if (user.getRoles().contains(userRole)) {
            throw new UserAlreadyHasRoleException("User already has role");
        }

        Set<UserRole> roles = user.getRoles();

        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);
        return user;
    }

}
