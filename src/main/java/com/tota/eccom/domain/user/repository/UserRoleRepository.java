package com.tota.eccom.domain.user.repository;

import com.tota.eccom.domain.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
    Optional<UserRole> findByName(String name);

    @Query("SELECT ur FROM UserRole ur WHERE ur.status = 'ACTIVE'")
    List<UserRole> findAllActive();
}
