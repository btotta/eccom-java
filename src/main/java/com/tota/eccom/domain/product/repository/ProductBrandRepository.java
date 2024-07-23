package com.tota.eccom.domain.product.repository;


import com.tota.eccom.domain.product.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long>, JpaSpecificationExecutor<ProductBrand> {
    Optional<ProductBrand> findBySlug(String slug);
}
