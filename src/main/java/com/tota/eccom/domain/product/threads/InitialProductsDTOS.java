package com.tota.eccom.domain.product.threads;

import com.tota.eccom.domain.product.model.*;
import com.tota.eccom.util.SlugUtil;
import com.tota.eccom.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class InitialProductsDTOS {


    private Product camiseta;
    private Product bermuda;

    public Product getCamiseta() {

        ProductStock productStock = ProductStock.builder()
                .quantity(1000)
                .reservedQuantity(10)
                .build();

        ProductPrice productPrice1 = ProductPrice.builder()
                .price(BigDecimal.valueOf(20F))
                .quantity(1)
                .build();

        ProductPrice productPrice2 = ProductPrice.builder()
                .price(BigDecimal.valueOf(16F))
                .quantity(3)
                .build();

        ProductPrice productPrice3 = ProductPrice.builder()
                .price(BigDecimal.valueOf(12F))
                .quantity(6)
                .build();

        return Product.builder()
                .name("Camiseta cor preta de algodao")
                .sku("00000000000001010-UN")
                .description("Camiseta de algodao")
                .slug(SlugUtil.makeSlug("Camiseta cor preta de algodao"))
                .familyCode("1010")
                .materialGroup("1010")
                .packageType("UN")
                .conversionFactor(1.0)
                .height(1.0)
                .width(1.0)
                .length(1.0)
                .grossWeight(1.0)
                .wholesaleQuantity(1)
                .ean("1010")
                .productPrices(List.of(productPrice1, productPrice2, productPrice3))
                .productStock(productStock)
                .status(Status.ACTIVE)
                .build();
    }

    public Product getBermuda() {

        ProductStock productStock = ProductStock.builder()
                .quantity(5000)
                .reservedQuantity(50)
                .build();

        ProductPrice productPrice1 = ProductPrice.builder()
                .price(BigDecimal.valueOf(20F))
                .quantity(1)
                .build();

        ProductPrice productPrice2 = ProductPrice.builder()
                .price(BigDecimal.valueOf(16F))
                .quantity(3)
                .build();


        return Product.builder()
                .name("Bermuda cor cinza de moleton")
                .sku("00000000000001020-UN")
                .slug(SlugUtil.makeSlug("Bermuda cor cinza de moleton"))
                .description("Bermuda de moleton")
                .familyCode("1020")
                .materialGroup("1020")
                .packageType("UN")
                .conversionFactor(1.0)
                .height(1.0)
                .width(1.0)
                .length(1.0)
                .grossWeight(1.0)
                .wholesaleQuantity(1)
                .ean("1020")
                .productPrices(List.of(productPrice1, productPrice2))
                .productStock(productStock)
                .status(Status.ACTIVE)
                .build();
    }

    public ProductBrand getBrand() {
        return ProductBrand.builder()
                .name("Home Brand")
                .description("Brand for things made by me")
                .slug(SlugUtil.makeSlug("Home Brand"))
                .status(Status.ACTIVE)
                .build();
    }

    public ProductCategory getCategory() {
        return ProductCategory.builder()
                .name("Clothes Brand")
                .description("Clothes made by me from home")
                .slug(SlugUtil.makeSlug("Clothes Brand"))
                .status(Status.ACTIVE)
                .build();
    }




}
