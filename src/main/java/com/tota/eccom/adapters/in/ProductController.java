package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for product management")
public class ProductController {

    private final IProductDomain productDomain;

    @GetMapping("/public/{id}")
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

    @GetMapping("/public/paginated")
    @PageableAsQueryParam
    @Operation(
            summary = "Get all products paginated",
            description = "Retrieves a paginated list of products based on the provided filters."
    )
    @Parameter(name = "name", description = "Filter products by name", example = "Product1")
    @Parameter(name = "description", description = "Filter products by description", example = "A great product")
    @Parameter(name = "price", description = "Filter products by price", example = "19.99")
    @Parameter(name = "brand", description = "Filter products by brand", example = "BrandX")
    @Parameter(name = "category", description = "Filter products by category", example = "Electronics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<Page<Product>> getAllProductsPaginated(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category
    ) {
        return ResponseEntity.ok(productDomain.getAllProductsPaginated(pageable, name, description, price, brand, category));
    }


    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCreate productCreateDTO) {
        return new ResponseEntity<>(productDomain.createProduct(productCreateDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product by id",
            description = "Deletes the product with the specified ID.",
            security = @SecurityRequirement(name = "bearerAuth")
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
    @Operation(
            summary = "Update product by id",
            description = "Updates the product with the specified ID using the provided details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductUpdate productUpdateDTO) {
        return new ResponseEntity<>(productDomain.updateProductById(id, productUpdateDTO), HttpStatus.OK);
    }

    @PostMapping("/price/{id}")
    @Operation(
            summary = "Add price to product",
            description = "Adds a price to the product with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Price added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Product> addPriceToProduct(@PathVariable Long id, @RequestBody @Valid ProductCreatePrice productCreatePriceDTO) {
        return new ResponseEntity<>(productDomain.addPriceToProduct(id, productCreatePriceDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/price/{priceId}")
    @Operation(
            summary = "Delete price from product",
            description = "Deletes the price from the product with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Price deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deletePriceFromProduct(@PathVariable Long id, @PathVariable Long priceId) {
        productDomain.deletePriceFromProduct(id, priceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
