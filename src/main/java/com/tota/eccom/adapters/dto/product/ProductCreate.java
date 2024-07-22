package com.tota.eccom.adapters.dto.product;

import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPackage;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProductCreate {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Plu is required")
    private String plu;

    @NotEmpty(message = "Family code is required")
    private String familyCode;

    private String materialGroup;

    private String packageDefault;

    private Integer lockCode;

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


    public Product toProduct() {

        validateCreateProduct();

        List<ProductPackage> productPackages = new ArrayList<>();

        ProductPackage productPackage = ProductPackage.builder()
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

        productPackages.add(productPackage);

        return Product.builder()
                .name(name)
                .plu(plu)
                .familyCode(familyCode)
                .materialGroup(materialGroup)
                .packageDefault(packageDefault)
                .lockCode(lockCode)
                .productPackages(productPackages)
                .build();

    }

    private void validateCreateProduct() {

        validateField(name, "Name is required");
        validateField(plu, "Plu is required");
        validateField(familyCode, "Family code is required");
        validateField(type, "Type is required");
    }

    private void validateField(String field, String message) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }


}
