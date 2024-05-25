package com.tota.eccom.domain.product.model;

import com.tota.eccom.domain.common.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product",
        indexes = {
                @Index(name = "product_name_index", columnList = "name"),
                @Index(name = "product_category_index", columnList = "category"),
                @Index(name = "product_brand_index", columnList = "brand"),
                @Index(name = "product_status_index", columnList = "status"),
                @Index(name = "product_stock_index", columnList = "stock")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "stock")
    private Integer stock;

    @NotEmpty
    @Column(name = "category", nullable = false)
    private String category;

    @NotEmpty
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotEmpty
    @Column(name = "brand", nullable = false)
    private String brand;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPrice> prices;

    @NotNull
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
