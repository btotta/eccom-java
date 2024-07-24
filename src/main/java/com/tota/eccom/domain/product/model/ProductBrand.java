package com.tota.eccom.domain.product.model;

import com.tota.eccom.util.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "product_brand", indexes = {
        @Index(name = "idx_product_brand_name", columnList = "name"),
        @Index(name = "idx_product_brand_slug", columnList = "slug"),
        @Index(name = "idx_product_brand_description", columnList = "description"),
        @Index(name = "idx_product_brand_status", columnList = "status"),
        @Index(name = "idx_product_brand_created_at", columnList = "createdAt"),
        @Index(name = "idx_product_brand_updated_at", columnList = "updatedAt"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
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
