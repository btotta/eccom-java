package com.tota.eccom.domain.product.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "product_stock", indexes = {
        @Index(name = "idx_product_stock_product_id", columnList = "product_id"),
        @Index(name = "idx_product_stock_quantity", columnList = "quantity"),
        @Index(name = "idx_product_stock_compromised_quantity", columnList = "compromised_quantity"),
        @Index(name = "idx_product_stock_available_quantity", columnList = "available_quantity"),
        @Index(name = "idx_product_stock_reserved_quantity", columnList = "reserved_quantity"),
        @Index(name = "idx_product_stock_reserved_quantity_to_date", columnList = "reserved_quantity_to_date"),
})
@Data
@NoArgsConstructor
@Getter
@Setter
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
