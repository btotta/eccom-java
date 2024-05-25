package com.tota.eccom.adapters.dto.product;

import com.tota.eccom.domain.product.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdate {

    private String name;
    private String description;
    private String category;
    private String brand;
    private String sku;

    public void updateProduct(Product product) {
        if (product.getName() != null) {
            this.name = product.getName();
        }

        if (product.getDescription() != null) {
            this.description = product.getDescription();
        }

        if (product.getCategory() != null) {
            this.category = product.getCategory();
        }

        if (product.getBrand() != null) {
            this.brand = product.getBrand();
        }

        if (product.getSku() != null) {
            this.sku = product.getSku();
        }
    }
}
