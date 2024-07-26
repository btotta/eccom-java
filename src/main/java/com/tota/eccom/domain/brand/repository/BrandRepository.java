package com.tota.eccom.domain.brand.repository;


import com.tota.eccom.domain.brand.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    Optional<Brand> findBySlug(String slug);
}
