package com.tota.eccom.domain.user.business;

import com.tota.eccom.adapters.dto.user.request.UserRoleCreateDTO;
import com.tota.eccom.domain.user.IRoleService;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.RoleRepository;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.user.UserAlreadyHasRoleException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
import com.tota.eccom.exceptions.user.UserRoleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @Override
    public List<Role> getAllUserRoles() {
        return roleRepository.findAllActive();
    }

    @Transactional
    @Override
    public Role createUserRole(UserRoleCreateDTO userRoleCreateDTO) {
        Role userRole = userRoleCreateDTO.toUserRole();
        return roleRepository.save(userRole);
    }

    @Override
    public Role getUserRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found with given id: " + id));
    }

    @Transactional
    @Override
    public User associateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with given id: " + userId));

        Role userRole = getUserRoleById(roleId);

        if (user.getRoles().contains(userRole)) {
            throw new UserAlreadyHasRoleException("User already has role");
        }

        user.getRoles().add(userRole);

        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void deleteUserRoleById(Long id) {
        Role userRole = getUserRoleById(id);
        roleRepository.delete(userRole);
    }
}
