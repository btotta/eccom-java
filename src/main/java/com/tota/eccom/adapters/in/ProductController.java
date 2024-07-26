package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.adapters.dto.product.response.ProductRespDTO;
import com.tota.eccom.domain.product.IProductService;
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
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for product management")
public class ProductController {

    private final IProductService productDomain;

    // Product Admin Operations
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.createProduct(productDTO)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get product by id",
            description = "Retrieves the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    public ResponseEntity<ProductRespDTO> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.getProductById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete product by id",
            description = "Deletes the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productDomain.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update product by id",
            description = "Updates the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.updateProductById(id, productDTO)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update product by id",
            description = "Updates the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> patchProductById(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.patchProductById(id, productDTO)), HttpStatus.OK);
    }


    //Product Price Admin Operations
    @PostMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Add product price to product",
            description = "Adds a product price to the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product price added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> addProductPriceToProduct(@PathVariable Long id, @RequestBody @Valid ProductPriceDTO productPriceDTO) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.addProductPriceToProduct(id, productPriceDTO)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/price/{priceId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete product price from product",
            description = "Deletes the product price from the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product price deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteProductPriceFromProduct(@PathVariable Long id, @PathVariable Long priceId) {
        productDomain.deleteProductPriceFromProduct(id, priceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //Product Stock Admin Operations
    @PostMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Add product stock to product",
            description = "Adds a product stock to the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product stock added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> addProductStockToProduct(@PathVariable Long id, @RequestBody @Valid ProductStockDTO productStockDTO) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.addProductStockToProduct(id, productStockDTO)), HttpStatus.CREATED);
    }

    // Product n Category Operations
    @PostMapping("/{id}/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Add product category to product",
            description = "Adds a product category to the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product category added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> addProductCategoryToProduct(@PathVariable Long id, @PathVariable Long categoryId) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.addProductCategoryToProduct(id, categoryId)), HttpStatus.CREATED);
    }

    // Product n Brand Operations
    @PostMapping("/{id}/brand/{brandId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Add product brand to product",
            description = "Adds a product brand to the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product brand added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductRespDTO> addProductBrandToProduct(@PathVariable Long id, @PathVariable Long brandId) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.addProductBrandToProduct(id, brandId)), HttpStatus.CREATED);
    }

    // Product Public Store Operations
    @GetMapping("{slug}")
    @Operation(
            summary = "Get product by slug",
            description = "Retrieves the product with the specified slug."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductRespDTO> getProductBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(new ProductRespDTO(productDomain.getProductBySlug(slug)), HttpStatus.OK);
    }


    @GetMapping("/search/{term}/pagination")
    @Operation(
            summary = "Search products by term",
            description = "Retrieves a list of products that match the specified term."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ProductRespDTO>> searchProductsByTerm(
            @PathVariable String term,
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        return new ResponseEntity<>(productDomain.searchProductsByTerm(term, pageable).map(ProductRespDTO::new), HttpStatus.OK);
    }

}
