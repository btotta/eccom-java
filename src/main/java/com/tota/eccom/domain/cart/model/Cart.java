package com.tota.eccom.domain.cart.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tota.eccom.domain.cart.model.enums.CartStatus;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart")
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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
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
