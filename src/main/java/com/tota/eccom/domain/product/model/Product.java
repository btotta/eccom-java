package com.tota.eccom.domain.product.model;

import com.tota.eccom.util.enums.Status;
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
        @Index(name = "idx_product_sku", columnList = "sku"),
        @Index(name = "idx_product_family_code", columnList = "familyCode"),
        @Index(name = "idx_product_material_group", columnList = "materialGroup"),
        @Index(name = "idx_product_package_type", columnList = "packageType"),
        @Index(name = "idx_product_conversion_factor", columnList = "conversionFactor"),
        @Index(name = "idx_product_height", columnList = "height"),
        @Index(name = "idx_product_width", columnList = "width"),
        @Index(name = "idx_product_length", columnList = "length"),
        @Index(name = "idx_product_gross_weight", columnList = "grossWeight"),
        @Index(name = "idx_product_wholesale_quantity", columnList = "wholesaleQuantity"),
        @Index(name = "idx_product_ean", columnList = "ean"),
        @Index(name = "idx_product_status", columnList = "status"),
        @Index(name = "idx_product_created_at", columnList = "createdAt"),
        @Index(name = "idx_product_updated_at", columnList = "updatedAt"),
        @Index(name = "idx_product_slug", columnList = "slug"),
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
    private String slug;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String sku;

    private String familyCode;

    @Column
    private String materialGroup;

    @Column(nullable = false)
    private String packageType;

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
    private String ean;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductPrice> productPrices = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ProductStock productStock;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category_mapping",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id")
    )
    private List<ProductCategory> productCategories = new ArrayList<>();

    @ManyToOne
    private ProductBrand productBrand;

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
