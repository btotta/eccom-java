package com.tota.eccom.adapters.dto.product.request;

import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.util.SlugUtil;
import lombok.Builder;
import lombok.Data;

import java.util.function.Consumer;

@Data
@Builder
public class ProductDTO {

    private String name;
    private String description;
    private String sku;
    private String familyCode;
    private String materialGroup;
    private String packageType;
    private Double conversionFactor;
    private Double height;
    private Double width;
    private Double length;
    private Double grossWeight;
    private Integer wholesaleQuantity;
    private String ean;
    private Status status;

    public Product toProduct() {

        validateCreateProduct();

        return Product.builder()
                .name(name)
                .slug(SlugUtil.makeSlug(name))
                .description(description)
                .sku(sku)
                .familyCode(familyCode)
                .materialGroup(materialGroup)
                .packageType(packageType)
                .conversionFactor(conversionFactor)
                .height(height)
                .width(width)
                .length(length)
                .grossWeight(grossWeight)
                .wholesaleQuantity(wholesaleQuantity)
                .ean(ean)
                .status(Status.ACTIVE)
                .build();
    }

    public Product toUpdatedProduct(Product product) {

        validateCreateProduct();

        product.setName(name);
        product.setDescription(description);
        product.setSku(sku);
        product.setFamilyCode(familyCode);
        product.setMaterialGroup(materialGroup);
        product.setPackageType(packageType);
        product.setConversionFactor(conversionFactor);
        product.setHeight(height);
        product.setWidth(width);
        product.setLength(length);
        product.setGrossWeight(grossWeight);
        product.setWholesaleQuantity(wholesaleQuantity);
        product.setEan(ean);

        if (status != null) {
            product.setStatus(status);
        }

        return product;
    }

    public Product toPatchedProduct(Product product) {

        setAttribute(name, product::setName);
        setAttribute(description, product::setDescription);
        setAttribute(sku, product::setSku);
        setAttribute(familyCode, product::setFamilyCode);
        setAttribute(materialGroup, product::setMaterialGroup);
        setAttribute(packageType, product::setPackageType);
        setAttribute(conversionFactor, product::setConversionFactor);
        setAttribute(height, product::setHeight);
        setAttribute(width, product::setWidth);
        setAttribute(length, product::setLength);
        setAttribute(grossWeight, product::setGrossWeight);
        setAttribute(wholesaleQuantity, product::setWholesaleQuantity);
        setAttribute(ean, product::setEan);

        return product;
    }


    private void validateCreateProduct() {

        validateField(name, "Name is required");
        validateField(sku, "SKU is required");
        validateField(description, "Description is required");
        validateField(packageType, "Package type is required");
    }

    private void validateField(String field, String message) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void setAttribute(T attribute, Consumer<T> setter) {

        if (attribute == null) {
            return;
        }

        if (attribute instanceof String strAttribute) {
            if (!strAttribute.isBlank()) {
                setter.accept(attribute);
            }
        } else {
            setter.accept(attribute);
        }

    }


}
