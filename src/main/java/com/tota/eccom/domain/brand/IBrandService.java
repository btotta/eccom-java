package com.tota.eccom.domain.brand;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.brand.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface IBrandService {


    Brand createBrand(BrandDTO brandDTO);

    Brand getBrandById(Long id);

    void deleteBrandById(Long id);

    Brand updateBrandById(Long id, BrandDTO brandDTO);

    Brand getBrandBySlug(String slug);

    Page<Product> getProductsByBrand(String slug, Pageable pageable);
}
