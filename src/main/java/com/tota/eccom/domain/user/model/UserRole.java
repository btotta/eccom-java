package com.tota.eccom.domain.user.model;

import com.tota.eccom.domain.common.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "name"),
        @Index(name = "idx_role_created_at", columnList = "created_at"),
        @Index(name = "idx_role_updated_at", columnList = "updated_at"),
        @Index(name = "idx_role_created_at_updated_at", columnList = "created_at,updated_at"),
        @Index(name = "idx_role_updated_at_created_at", columnList = "updated_at,created_at"),
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

}
