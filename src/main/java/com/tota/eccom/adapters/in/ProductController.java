package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductCreateProductPackage;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    private final IProductDomain productDomain;


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
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCreate productCreateDTO) {
        return new ResponseEntity<>(productDomain.createProduct(productCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{plu}")
    @Operation(
            summary = "Get product by plu",
            description = "Retrieves the product with the specified plu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductByPlu(@PathVariable String plu) {
        return new ResponseEntity<>(productDomain.getProductByPLU(plu), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by id",
            description = "Retrieves the product with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productDomain.getProductById(id), HttpStatus.OK);
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

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Add product package to product",
            description = "Adds a product package to the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product package added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Product> addProductPackageToProduct(@PathVariable Long id, @RequestBody @Valid ProductCreateProductPackage productCreateProductPackage) {
        return new ResponseEntity<>(productDomain.addProductPackageToProduct(id, productCreateProductPackage), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/package/{packageId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete product package from product",
            description = "Deletes the product package from the product with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product package deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteProductPackageFromProduct(@PathVariable Long id, @PathVariable Long packageId) {
        productDomain.deleteProductPackageFromProduct(id, packageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
