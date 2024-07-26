package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.category.request.CategoryDTO;
import com.tota.eccom.adapters.dto.category.response.CategoryRespDTO;
import com.tota.eccom.adapters.dto.product.response.ProductRespDTO;
import com.tota.eccom.domain.category.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Endpoints for category management")
public class CategoryController {


    private final ICategoryService categoryDomain;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new category",
            description = "Creates a new category with the provided details.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<CategoryRespDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return new ResponseEntity<>(new CategoryRespDTO(categoryDomain.createCategory(categoryDTO)), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get category by id",
            description = "Retrieves the category with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    public ResponseEntity<CategoryRespDTO> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(new CategoryRespDTO(categoryDomain.getCategoryById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete category by id",
            description = "Deletes the category with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryDomain.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update category by id",
            description = "Updates the category with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<CategoryRespDTO> updateCategoryById(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        return new ResponseEntity<>(new CategoryRespDTO(categoryDomain.updateCategoryById(id, categoryDTO)), HttpStatus.OK);
    }


    // Category n Product Operations
    @GetMapping("/{slug}/products")
    @Operation(
            summary = "Get products by category",
            description = "Retrieves the products with the specified category."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ProductRespDTO>> getProductsByCategory(@PathVariable String slug, @PageableDefault(size = 20, page = 0) Pageable pageable) {
        return new ResponseEntity<>(categoryDomain.getProductsByCategory(slug, pageable).map(ProductRespDTO::new), HttpStatus.OK);
    }






}
