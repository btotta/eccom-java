package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.product.IProductCategoryDomain;
import com.tota.eccom.domain.product.model.ProductCategory;
import com.tota.eccom.domain.product.repository.ProductCategoryRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.SlugUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryDomain implements IProductCategoryDomain {

    private final ProductCategoryRepository productCategoryRepository;


    @Override
    public ProductCategory createCategory(CategoryDTO categoryDTO) {

        if (categoryDTO.getName() != null && findCategoryBySlug(SlugUtil.makeSlug(categoryDTO.getName())) != null) {
            throw new ResourceAlreadyExistsException("Category with given slug already exists");
        }

        ProductCategory productCategory = categoryDTO.toCategory();

        log.info("Creating category: {}", productCategory);

        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory getCategoryById(Long id) {

        return productCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id: " + id));
    }

    @Override
    public void deleteCategoryById(Long id) {

        ProductCategory category = getCategoryById(id);

        log.info("Deleting category by id: {}", id);

        category.setStatus(Status.DELETED);

        productCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public ProductCategory updateCategoryById(Long id, CategoryDTO categoryDTO) {

        ProductCategory category = getCategoryById(id);

        if (categoryDTO.getName() != null) {
            validateExistingSlug(SlugUtil.makeSlug(categoryDTO.getName()), id);
        }

        categoryDTO.toUpdatedCategory(category);

        log.info("Updating category: {}", category);

        return productCategoryRepository.save(category);
    }

    private void validateExistingSlug(String slug, Long id) {

        ProductCategory existingCategory = findCategoryBySlug(slug);
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Category with given slug already exists");
        }

    }

    private ProductCategory findCategoryBySlug(String slug) {
        return productCategoryRepository.findBySlug(slug).orElse(null);
    }
}
