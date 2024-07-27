package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserCreateDTO;
import com.tota.eccom.adapters.dto.user.request.UserLoginDTO;
import com.tota.eccom.adapters.dto.user.request.UserUpdateDTO;
import com.tota.eccom.adapters.dto.user.response.UserLoginRespDTO;
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
import com.tota.eccom.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        if (getUserByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new UserEmailExistsException("User already exists with given email.");
        }

        Role userRole = getUserRole();

        User user = userCreateDTO.toUser();
        user.getRoles().add(userRole);

        log.info("Creating user: {}", user);

        return userRepository.save(user);
    }

    private Optional<User> getUserByEmail(String email) {
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

        Optional<User> existingUser = getUserByEmail(userUpdateDTO.getEmail());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
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

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
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
        Optional<User> user = userRepository.findByEmail(userLoginDTO.getEmail());

        if (user.isEmpty() || !PasswordUtil.validatePassword(userLoginDTO.getPassword(), user.get().getPassword())) {
            throw new UserNotFoundException("User not found with given email and password");
        }

        String token = jwtTokenUtil.generateToken(user.get());
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.get());

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

        InvalidJwtTokenUtil.addToken(token);
    }

    @Override
    public UserLoginRespDTO refreshUserLogin(String refreshToken) {

        String userEmail = jwtTokenUtil.getUsernameFromToken(refreshToken);

        if (userEmail == null) {
            throw new UserNotFoundException("User not found");
        }

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtTokenUtil.generateToken(user);

        return UserLoginRespDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
