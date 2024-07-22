package com.tota.eccom.adapters.dto.product;

import com.tota.eccom.domain.product.model.ProductPackage;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateProductPackage {

    private Long productId;
    @NotEmpty(message = "Type is required")
    private String type;
    private Double conversionFactor;
    private Double height;
    private Double width;
    private Double length;
    private Double grossWeight;
    private Integer wholesaleQuantity;
    private String palletBallastHeight;
    private String ean;

    public ProductPackage toProductPackage() {

        valiteAddProductPackage();

        return ProductPackage.builder()
                .type(type)
                .conversionFactor(conversionFactor)
                .height(height)
                .width(width)
                .length(length)
                .grossWeight(grossWeight)
                .wholesaleQuantity(wholesaleQuantity)
                .palletBallastHeight(palletBallastHeight)
                .ean(ean)
                .build();
    }

    private void valiteAddProductPackage() {
        validateField(type, "Type is required");

        if (productId == null || productId == 0) {
            throw new IllegalArgumentException("Product ID is required");
        }

    }

    private void validateField(String field, String message) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }


}
