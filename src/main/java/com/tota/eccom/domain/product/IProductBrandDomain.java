package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IProductBrandDomain {


    ProductBrand createBrand(BrandDTO brandDTO);

    ProductBrand getBrandById(Long id);

    void deleteBrandById(Long id);

    ProductBrand updateBrandById(Long id, BrandDTO brandDTO);

    ProductBrand getBrandBySlug(String slug);

    Page<Product> getProductsByBrand(String slug, Pageable pageable);
}
