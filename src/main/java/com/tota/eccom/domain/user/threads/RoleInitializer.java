package com.tota.eccom.domain.user.threads;

import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void initializeRoles() {
        createRoleIfNotFound("USER");
        createRoleIfNotFound("ADMIN");
    }

    private void createRoleIfNotFound(String role) {
        Role roleFound = roleRepository.findByName(role).orElseGet(() -> {
            Role r = Role.builder()
                    .name(role)
                    .status(Status.ACTIVE)
                    .build();

            log.info("Creating role: {}", role);
            return roleRepository.save(r);
        });
        log.info("Role {} already exists", roleFound);
    }
}
