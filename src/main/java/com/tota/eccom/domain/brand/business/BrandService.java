package com.tota.eccom.domain.brand.business;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.brand.IBrandService;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.SlugUtil;
import com.tota.eccom.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandService implements IBrandService {


    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public Brand createBrand(BrandDTO brandDTO) {

        if (brandDTO.getName() != null && findBrandBySlug(SlugUtil.makeSlug(brandDTO.getName())) != null) {
            throw new ResourceAlreadyExistsException("Brand with given slug already exists");
        }

        Brand brand = brandDTO.toBrand();

        log.info("Creating brand: {}", brand);

        return brandRepository.save(brand);
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with given id: " + id));
    }

    @Override
    @Transactional
    public void deleteBrandById(Long id) {
        Brand brand = getBrandById(id);

        log.info("Deleting brand by id: {}", id);

        brand.setStatus(Status.DELETED);

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public Brand updateBrandById(Long id, BrandDTO brandDTO) {

        Brand brand = getBrandById(id);

        if (brandDTO.getName() != null) {
            validateExistingSlug(SlugUtil.makeSlug(brandDTO.getName()), id);
        }

        brandDTO.toUpdatedBrand(brand);

        log.info("Updating brand: {}", brand);

        return brandRepository.save(brand);
    }

    @Override
    public Brand getBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Brand not found with given slug: " + slug));
    }

    @Override
    public Page<Product> getProductsByBrand(String slug, Pageable pageable) {

        Brand brand = getBrandBySlug(slug);

        if (brand == null) {
            throw new ResourceNotFoundException("Brand not found with given slug: " + slug);
        }

        return productRepository.findByProductsByBrandId(brand.getId(), pageable);
    }

    private void validateExistingSlug(String slug, Long id) {
        Brand existingBrand = findBrandBySlug(slug);
        if (existingBrand != null && !existingBrand.getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Brand with given slug already exists");
        }
    }

    private Brand findBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug).orElse(null);
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

}
