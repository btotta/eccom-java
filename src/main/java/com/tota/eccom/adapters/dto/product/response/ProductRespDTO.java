package com.tota.eccom.adapters.dto.product.response;


import com.tota.eccom.domain.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRespDTO {

    private Long id;
    private String name;
    private String slug;
    private String familyCode;
    private String materialGroup;
    private String packageType;
    private Double conversionFactor;
    private Double height;
    private Double width;
    private Double length;
    private Double grossWeight;
    private Integer wholesaleQuantity;
    private String palletBallastHeight;
    private String ean;
    private Integer lockCode;
    private String description;
    private String sku;
    private Date createdAt;
    private Date updatedAt;
    private List<ProductPriceRespDTO> productPrices;
    private ProductStockRespDTO productStock;


    public ProductRespDTO(Product p) {

        this.id = p.getId();
        this.name = p.getName();
        this.slug = p.getSlug();
        this.familyCode = p.getFamilyCode();
        this.materialGroup = p.getMaterialGroup();
        this.packageType = p.getPackageType();
        this.conversionFactor = p.getConversionFactor();
        this.height = p.getHeight();
        this.width = p.getWidth();
        this.length = p.getLength();
        this.grossWeight = p.getGrossWeight();
        this.wholesaleQuantity = p.getWholesaleQuantity();
        this.palletBallastHeight = p.getPalletBallastHeight();
        this.ean = p.getEan();
        this.lockCode = p.getLockCode();
        this.description = p.getDescription();
        this.sku = p.getSku();
        this.createdAt = p.getCreatedAt();
        this.updatedAt = p.getUpdatedAt();
        this.productPrices = p.getProductPrices().stream().map(ProductPriceRespDTO::new).toList();
        this.productStock = new ProductStockRespDTO(p.getProductStock());
    }

}
