package com.tota.eccom.domain.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_price",
        indexes = {
                @Index(name = "product_price_price_index", columnList = "price"),
                @Index(name = "product_price_quantity_index", columnList = "quantity"),
                @Index(name = "product_price_price_quantity_index", columnList = "price,quantity")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
