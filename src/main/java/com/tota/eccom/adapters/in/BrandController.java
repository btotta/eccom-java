package com.tota.eccom.adapters.in;


import com.tota.eccom.adapters.dto.brand.request.BrandDTO;
import com.tota.eccom.adapters.dto.brand.response.BrandRespDTO;
import com.tota.eccom.adapters.dto.product.response.ProductRespDTO;
import com.tota.eccom.domain.brand.IBrandService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Tag(name = "Brand", description = "Endpoints for brand management")
public class BrandController {

    private final IBrandService brandDomain;


    @PostMapping
    @Operation(
            summary = "Create a new brand",
            description = "Creates a new brand with the provided details.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Brand created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<BrandRespDTO> createBrand(@RequestBody @Valid BrandDTO brandDTO) {
        return new ResponseEntity<>(new BrandRespDTO(brandDomain.createBrand(brandDTO)), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get brand by id",
            description = "Retrieves the brand with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    public ResponseEntity<BrandRespDTO> getBrandById(@PathVariable Long id) {
        return new ResponseEntity<>(new BrandRespDTO(brandDomain.getBrandById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete brand by id",
            description = "Deletes the brand with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteBrandById(@PathVariable Long id) {
        brandDomain.deleteBrandById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update brand by id",
            description = "Updates the brand with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Brand not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<BrandRespDTO> updateBrandById(@PathVariable Long id, @RequestBody @Valid BrandDTO brandDTO) {
        return new ResponseEntity<>(new BrandRespDTO(brandDomain.updateBrandById(id, brandDTO)), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    @Operation(
            summary = "Get brand by slug",
            description = "Retrieves the brand with the specified slug."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<BrandRespDTO> getBrandBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(new BrandRespDTO(brandDomain.getBrandBySlug(slug)), HttpStatus.OK);
    }

    // Brand n Product Operations
    @GetMapping("/{slug}/products")
    @Operation(
            summary = "Get products by brand",
            description = "Retrieves the products with the specified brand."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ProductRespDTO>> getProductsByBrand(@PathVariable String slug, @PageableDefault(size = 20, page = 0) Pageable pageable) {
        return new ResponseEntity<>(brandDomain.getProductsByBrand(slug, pageable).map(ProductRespDTO::new), HttpStatus.OK);
    }


}
