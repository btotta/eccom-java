package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.domain.product.IProductBrandDomain;
import com.tota.eccom.domain.product.model.ProductBrand;
import com.tota.eccom.domain.product.repository.ProductBrandRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.SlugUtil;
import com.tota.eccom.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductBrandDomain implements IProductBrandDomain {


    private final ProductBrandRepository productBrandRepository;


    @Override
    @Transactional
    public ProductBrand createBrand(BrandDTO brandDTO) {

        if (brandDTO.getName() != null && findBrandBySlug(SlugUtil.makeSlug(brandDTO.getName())) != null) {
            throw new ResourceAlreadyExistsException("Brand with given slug already exists");
        }

        ProductBrand productBrand = brandDTO.toBrand();

        log.info("Creating brand: {}", productBrand);

        return productBrandRepository.save(productBrand);
    }

    @Override
    public ProductBrand getBrandById(Long id) {
        return productBrandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with given id: " + id));
    }

    @Override
    @Transactional
    public void deleteBrandById(Long id) {
        ProductBrand brand = getBrandById(id);

        log.info("Deleting brand by id: {}", id);

        brand.setStatus(Status.DELETED);

        productBrandRepository.save(brand);
    }

    @Override
    @Transactional
    public ProductBrand updateBrandById(Long id, BrandDTO brandDTO) {

        ProductBrand brand = getBrandById(id);

        if (brandDTO.getName() != null) {
            validateExistingSlug(SlugUtil.makeSlug(brandDTO.getName()), id);
        }

        brandDTO.toUpdatedBrand(brand);

        log.info("Updating brand: {}", brand);

        return productBrandRepository.save(brand);
    }

    @Override
    public ProductBrand getBrandBySlug(String slug) {
        return productBrandRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Brand not found with given slug: " + slug));
    }

    private void validateExistingSlug(String slug, Long id) {
        ProductBrand existingBrand = findBrandBySlug(slug);
        if (existingBrand != null && !existingBrand.getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Brand with given slug already exists");
        }
    }

    private ProductBrand findBrandBySlug(String slug) {
        return productBrandRepository.findBySlug(slug).orElse(null);
    }

    private ProductBrand findBrandById(Long id) {
        return productBrandRepository.findById(id).orElse(null);
    }

}
