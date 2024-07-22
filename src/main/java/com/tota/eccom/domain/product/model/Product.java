package com.tota.eccom.domain.product.model;

import com.tota.eccom.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_plu", columnList = "plu", unique = true),
        @Index(name = "idx_product_family_code", columnList = "familyCode"),
        @Index(name = "idx_product_material_group", columnList = "materialGroup"),
        @Index(name = "idx_product_package_default", columnList = "packageDefault"),
        @Index(name = "idx_product_lock_code", columnList = "lockCode"),
        @Index(name = "idx_product_created_at", columnList = "createdAt"),
        @Index(name = "idx_product_updated_at", columnList = "updatedAt")
})
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String plu;

    @Column(nullable = false)
    private String familyCode;

    @Column
    private String materialGroup;

    @Column
    private String packageDefault;

    @Column
    private Integer lockCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductPackage> productPackages = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(new Date());
        this.setUpdatedAt(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Date());
    }
}
