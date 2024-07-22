package com.tota.eccom.domain.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "product_price", indexes = {
        @Index(name = "product_price_price_index", columnList = "price"),
        @Index(name = "product_price_quantity_index", columnList = "quantity"),
        @Index(name = "product_price_price_quantity_index", columnList = "price,quantity")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

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
