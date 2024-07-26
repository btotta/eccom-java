package com.tota.eccom.domain.category;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(Long id);

    void deleteCategoryById(Long id);

    Category updateCategoryById(Long id, CategoryDTO categoryDTO);

    Page<Product> getProductsByCategory(String slug, Pageable pageable);
}
