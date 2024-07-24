package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.domain.product.model.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public interface IProductCategoryDomain {
    ProductCategory createCategory(CategoryDTO categoryDTO);

    ProductCategory getCategoryById(Long id);

    void deleteCategoryById(Long id);

    ProductCategory updateCategoryById(Long id, CategoryDTO categoryDTO);
}