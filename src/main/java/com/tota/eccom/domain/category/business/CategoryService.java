package com.tota.eccom.domain.category.business;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.domain.category.ICategoryService;
import com.tota.eccom.domain.category.model.Category;
import com.tota.eccom.domain.category.repository.CategoryRepository;
import com.tota.eccom.domain.product.model.Product;
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

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {

        if (categoryDTO.getName() != null && findCategoryBySlug(SlugUtil.makeSlug(categoryDTO.getName())).isPresent()) {
            throw new ResourceAlreadyExistsException("Category with given slug already exists");
        }

        Category category = categoryDTO.toCategory();

        log.info("Creating category: {}", category);

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id: " + id));
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {

        Category category = getCategoryById(id);

        log.info("Deleting category by id: {}", id);

        category.setStatus(Status.DELETED);

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategoryById(Long id, CategoryDTO categoryDTO) {

        Category category = getCategoryById(id);

        if (categoryDTO.getName() != null) {
            validateExistingSlug(SlugUtil.makeSlug(categoryDTO.getName()), id);
        }

        categoryDTO.toUpdatedCategory(category);

        log.info("Updating category: {}", category);

        return categoryRepository.save(category);
    }

    @Override
    public Page<Product> getProductsByCategory(String slug, Pageable pageable) {

        Optional<Category> category = findCategoryBySlug(slug);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found with given slug: " + slug);
        }

        return productRepository.findProductsByCategoryId(category.get().getId(), pageable);
    }

    @Override
    public Category setParentCategoryById(Long id, Long parentId) {

        if (Objects.equals(id, parentId)) {
            throw new ResourceAlreadyExistsException("Category cannot have itself as parent category");
        }

        Category category = getCategoryById(id);
        Category parentCategory = getCategoryById(parentId);

        log.info("Setting parent category id {} to category id {}", parentId, id);
        category.setParentCategory(parentCategory);

        return categoryRepository.save(category);
    }

    @Override
    public void removeParentCategoryById(Long id) {
        Category category = getCategoryById(id);

        log.info("Removing parent category from category id {}", id);
        category.setParentCategory(null);

        categoryRepository.save(category);
    }

    private void validateExistingSlug(String slug, Long id) {

        Optional<Category> existingCategory = findCategoryBySlug(slug);

        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Category with given slug already exists");
        }

    }

    private Optional<Category> findCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
}
