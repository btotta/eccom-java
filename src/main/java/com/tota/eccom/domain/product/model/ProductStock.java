package com.tota.eccom.domain.product.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "product_stock", indexes = {
        @Index(name = "idx_product_stock_quantity", columnList = "quantity"),
        @Index(name = "idx_product_stock_reserved_quantity", columnList = "reservedQuantity"),
        @Index(name = "idx_product_stock_created_at", columnList = "createdAt"),
        @Index(name = "idx_product_stock_updated_at", columnList = "updatedAt"),
})
@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reservedQuantity;

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
