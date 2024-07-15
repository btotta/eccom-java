package com.tota.eccom.domain.user.repository;

import com.tota.eccom.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String name);

    @Query("SELECT ur FROM Role ur WHERE ur.status = 'ACTIVE'")
    List<Role> findAllActive();
}
