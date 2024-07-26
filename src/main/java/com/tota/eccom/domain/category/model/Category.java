package com.tota.eccom.domain.category.model;

import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.util.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product_category", indexes = {
        @Index(name = "idx_product_category_name", columnList = "name"),
        @Index(name = "idx_product_category_slug", columnList = "slug"),
        @Index(name = "idx_product_category_created_at", columnList = "createdAt"),
        @Index(name = "idx_product_category_updated_at", columnList = "updatedAt"),
        @Index(name = "idx_product_category_status", columnList = "status"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
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

    @ManyToMany(mappedBy = "productCategories")
    private List<Product> products = new ArrayList<>();

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
