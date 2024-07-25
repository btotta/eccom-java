package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.user.IUserService;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.RoleRepository;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.user.UserEmailExistsException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.exceptions.user.UserRoleNotFoundException;
import com.tota.eccom.util.InvalidJwtTokenUtil;
import com.tota.eccom.util.JwtTokenUtil;
import com.tota.eccom.util.PasswordUtil;
import com.tota.eccom.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityUtil securityUtil;

    @Transactional
    @Override
    public User createUser(UserCreateDTO userCreateDTO) {
        if (getUserByEmail(userCreateDTO.getEmail()) != null) {
            throw new UserEmailExistsException("User already exists with given email.");
        }

        Role userRole = getUserRole();

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
                .orElseThrow(() -> new UserNotFoundException("User not found with given id: " + id));
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = getUserById(id);

        log.info("Deleting user by id: {}", id);

        user.setStatus(Status.DELETED);

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

        String username = securityUtil.getCurrentUsername();

        if (username == null) {
            throw new UserNotFoundException("User not logged");
        }

        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return user;
    }

    @Transactional
    @Override
    public void deleteUserLogged() {
        User user = getUserLogged();

        log.info("Deleting user: {}", user);

        user.setStatus(Status.DELETED);

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
    public Role getUserRole() {
        return roleRepository.findByName("USER")
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found with given name: USER"));
    }

    @Override
    public Role getAdminRole() {
        return roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found with given name: ADMIN"));
    }

    @Override
    public UserLoginRespDTO loginUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail());

        if (user == null || !PasswordUtil.validatePassword(userLoginDTO.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("User not found with given email and password");
        }

        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return UserLoginRespDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logoutUser() {

        String token = securityUtil.getCurrentJwtToken();

        if (token == null) {
            return;
        }

        User user = userRepository.findByEmail(jwtTokenUtil.getUsernameFromToken(token));

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        InvalidJwtTokenUtil.addToken(token);
    }

    @Override
    public UserLoginRespDTO refreshUserLogin(String refreshToken) {

        String userEmail = jwtTokenUtil.getUsernameFromToken(refreshToken);

        if (userEmail == null) {
            throw new UserNotFoundException("User not found");
        }

        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        String token = jwtTokenUtil.generateToken(user);

        return UserLoginRespDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
