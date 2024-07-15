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
        if (!isBlank(this.name)) {
            product.setName(product.getName().trim());
            product.setUrlName(product.getName().toLowerCase().replace(" ", "_"));
        }

        if (!isBlank(this.description)) {
            product.setDescription(product.getDescription().trim());
        }

        if (!isBlank(this.category)) {
            product.setCategory(product.getCategory().trim());
        }

        if (!isBlank(this.brand)) {
            product.setBrand(product.getBrand().trim());
        }

        if (!isBlank(this.sku)) {
            product.setSku(product.getSku().trim());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
