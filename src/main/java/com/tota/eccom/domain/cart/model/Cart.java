package com.tota.eccom.domain.cart.model;

import com.tota.eccom.domain.cart.model.enums.CartStatus;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart", indexes = {
        @Index(name = "idx_cart_user_id", columnList = "user_id"),
        @Index(name = "idx_cart_cart_status", columnList = "cart_status"),
        @Index(name = "idx_cart_total_items", columnList = "total_items"),
        @Index(name = "idx_cart_items_count", columnList = "items_count"),
        @Index(name = "idx_cart_total_order", columnList = "total_order"),
        @Index(name = "idx_cart_created_at", columnList = "createdAt"),
        @Index(name = "idx_cart_updated_at", columnList = "updatedAt"),
        @Index(name = "idx_cart_status", columnList = "status"),
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalItems;

    @Column
    private Integer itemsCount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrder;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

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
