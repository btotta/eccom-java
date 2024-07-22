package com.tota.eccom.domain.product.model;

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
@Entity
@Table(name = "product_package", indexes = {
        @Index(name = "idx_product_package_type", columnList = "type"),
        @Index(name = "idx_product_package_conversion_factor", columnList = "conversionFactor"),
        @Index(name = "idx_product_package_height", columnList = "height"),
        @Index(name = "idx_product_package_width", columnList = "width"),
        @Index(name = "idx_product_package_length", columnList = "length"),
        @Index(name = "idx_product_package_gross_weight", columnList = "grossWeight"),
        @Index(name = "idx_product_package_wholesale_quantity", columnList = "wholesaleQuantity"),
        @Index(name = "idx_product_package_pallet_ballast_height", columnList = "palletBallastHeight"),

})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column
    private Double conversionFactor;

    @Column
    private Double height;

    @Column
    private Double width;

    @Column
    private Double length;

    @Column
    private Double grossWeight;

    @Column
    private Integer wholesaleQuantity;

    @Column
    private String palletBallastHeight;

    @Column
    private String ean;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_package_id")
    private List<ProductPrice> productPrices = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_package_id")
    private List<ProductStock> productStocks = new ArrayList<>();

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