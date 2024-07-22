package com.tota.eccom.adapters.dto.product.request;

import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceDTO {

    private Double price;
    private Integer quantity;


    public Product addProductPriceToProduct(Product product) {

        validate();

        if (product.getProductPrices() == null) {
            product.setProductPrices(new ArrayList<>());
        }

        if (product.getProductPrices().stream().anyMatch(pp -> Objects.equals(pp.getQuantity(), quantity))) {
            throw new IllegalArgumentException("Product price with given quantity already exists");
        }

        product.getProductPrices().add(ProductPrice.builder()
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .build());

        return product;
    }

    public void validate() {
        if (price == null || quantity == null) {
            throw new IllegalArgumentException("Price and quantity are required");
        }

        if (price < 0F) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }


}
