package com.tota.eccom.domain.product.repository;

import com.tota.eccom.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE p.brand.id = :id")
    Page<Product> findByProductsByBrandId(Long id, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productCategories pc WHERE pc.id = :id")
    Page<Product> findProductsByCategoryId(Long id, Pageable pageable);
}
