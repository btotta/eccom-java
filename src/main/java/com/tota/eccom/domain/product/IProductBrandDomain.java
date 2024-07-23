package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.product.model.ProductBrand;
import org.springframework.stereotype.Component;

@Component
public interface IProductBrandDomain {


    ProductBrand createBrand(BrandDTO brandDTO);

    ProductBrand getBrandById(Long id);

    void deleteBrandById(Long id);

    ProductBrand updateBrandById(Long id, BrandDTO brandDTO);

    ProductBrand getBrandBySlug(String slug);
}
