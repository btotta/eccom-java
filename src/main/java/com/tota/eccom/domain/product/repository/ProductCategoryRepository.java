package com.tota.eccom.domain.product.repository;

import com.tota.eccom.domain.product.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {

    Optional<ProductCategory> findBySlug(String slug);

}
