package com.tota.eccom.adapters.dto.product;

import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreatePrice {

    private BigDecimal price;
    private Integer quantity;

    public ProductPrice toProductPrice() {
        return ProductPrice.builder()
                .price(price)
                .quantity(quantity)
                .build();
    }

    public void addPriceToProduct(Product product) {

        if (price == null || quantity == null) {
            throw new IllegalArgumentException("Price and quantity are required");
        }

        product.getPrices().add(toProductPrice());
    }
}
