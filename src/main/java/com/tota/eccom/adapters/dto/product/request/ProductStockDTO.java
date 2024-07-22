package com.tota.eccom.adapters.dto.product.request;


import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockDTO {

    private Integer quantity;


    public Product addProductStockToProduct(Product product) {

        validate();

        product.setProductStock(ProductStock.builder()
                .quantity(quantity)
                .reservedQuantity(0)
                .build());

        return product;

    }

    private void validate() {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity is required");
        }
    }

}
