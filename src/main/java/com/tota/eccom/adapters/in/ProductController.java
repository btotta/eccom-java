package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import io.swagger.v3.oas.annotations.Operation;
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
public class ProductController {

    private final IProductDomain productDomain;

    // Private routes
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCreate productCreateDTO) {
        return new ResponseEntity<>(productDomain.createProduct(productCreateDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by id")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productDomain.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update product by id")
    public ResponseEntity<Product> updateProductById(@PathVariable Long id, @RequestBody @Valid ProductUpdate productUpdateDTO) {
        return new ResponseEntity<>(productDomain.updateProductById(id, productUpdateDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/price/{id}")
    @Operation(summary = "Add price to product")
    public ResponseEntity<Product> addPriceToProduct(@PathVariable Long id, @RequestBody @Valid ProductCreatePrice productCreatePriceDTO) {
        return new ResponseEntity<>(productDomain.addPriceToProduct(id, productCreatePriceDTO), HttpStatus.OK);
    }

    // Public routes
    @GetMapping("/public/{id}")
    @Operation(summary = "Get product by id")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productDomain.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/public/paginated")
    @PageableAsQueryParam
    @Operation(summary = "Get all products paginated")
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
}
