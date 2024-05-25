package com.tota.eccom.adapters.dto.product;

import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.product.model.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductCreate {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Description is required")
    private String description;

    private List<ProductCreatePrice> prices;

    private Integer stock;

    @NotEmpty(message = "Category is required")
    private String category;

    @NotEmpty(message = "Brand is required")
    private String brand;

    @NotEmpty(message = "Sku is required")
    private String sku;

    public Product toProduct() {
        Status status = (prices == null || prices.isEmpty() || stock == null) ? Status.INACTIVE : Status.ACTIVE;

        return Product.builder()
                .name(name)
                .description(description)
                .stock(stock)
                .category(category)
                .sku(sku)
                .brand(brand)
                .status(status)
                .prices(prices == null ? null : prices.stream().map(ProductCreatePrice::toProductPrice).toList())
                .build();
    }
}
