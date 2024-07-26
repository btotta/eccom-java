package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.category.model.Category;
import com.tota.eccom.domain.category.repository.CategoryRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.SlugUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({ProductService.class})
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ProductService productService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    private ProductDTO getMockProductCreate() {
        return ProductDTO.builder()
                .name("Test Product")
                .description("Test Product Description")
                .sku("TEST-PRODUCT-SKU")
                .familyCode("TEST-FAMILY-CODE")
                .materialGroup("TEST-MATERIAL-GROUP")
                .packageType("TEST-PACKAGE-TYPE")
                .conversionFactor(1.0)
                .height(1.0)
                .width(1.0)
                .length(1.0)
                .grossWeight(1.0)
                .wholesaleQuantity(1)
                .ean("TEST-EAN")
                .build();
    }

    private ProductPriceDTO getMockProductPriceCreate() {
        return ProductPriceDTO.builder()
                .price(1.0)
                .quantity(1)
                .build();
    }

    private ProductStockDTO getMockProductStockCreate() {
        return ProductStockDTO.builder()
                .quantity(1)
                .build();
    }

    private Category getMockProductCategoryCreate() {
        return Category.builder()
                .name("Test Category")
                .description("Test Category Description")
                .slug(SlugUtil.makeSlug("Test Category"))
                .status(Status.ACTIVE)
                .build();
    }

    private Brand getMockProductBrandCreate() {
        return Brand.builder()
                .name("Test Brand")
                .description("Test Brand Description")
                .slug(SlugUtil.makeSlug("Test Brand"))
                .status(Status.ACTIVE)
                .build();
    }


    @Nested
    @DisplayName("Create Product")
    class CreateProductTest {

        @Test
        @DisplayName("Create product, should create product successfully")
        void testCreateProduct_shouldCreateProductSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());

            assertNotNull(createdProduct.getId());
            assertEquals(getMockProductCreate().getName(), createdProduct.getName());
            assertEquals(getMockProductCreate().getDescription(), createdProduct.getDescription());
            assertEquals(getMockProductCreate().getSku(), createdProduct.getSku());
            assertEquals(getMockProductCreate().getFamilyCode(), createdProduct.getFamilyCode());
            assertEquals(getMockProductCreate().getMaterialGroup(), createdProduct.getMaterialGroup());
            assertEquals(getMockProductCreate().getPackageType(), createdProduct.getPackageType());
            assertEquals(getMockProductCreate().getConversionFactor(), createdProduct.getConversionFactor());
            assertEquals(getMockProductCreate().getHeight(), createdProduct.getHeight());
            assertEquals(getMockProductCreate().getWidth(), createdProduct.getWidth());
            assertEquals(getMockProductCreate().getLength(), createdProduct.getLength());
            assertEquals(getMockProductCreate().getGrossWeight(), createdProduct.getGrossWeight());
            assertEquals(getMockProductCreate().getWholesaleQuantity(), createdProduct.getWholesaleQuantity());
            assertEquals(getMockProductCreate().getEan(), createdProduct.getEan());
            assertEquals(Status.ACTIVE, createdProduct.getStatus());
            assertNotNull(createdProduct.getCreatedAt());
            assertNotNull(createdProduct.getUpdatedAt());
            assertNotNull(createdProduct.getStatus());
        }

        @Test
        @DisplayName("Create product, should throw exception when name is null")
        void testCreateProduct_shouldThrowExceptionWhenNameIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when name is empty")
        void testCreateProduct_shouldThrowExceptionWhenNameIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when sku is null")
        void testCreateProduct_shouldThrowExceptionWhenSkuIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku(null);

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when sku is empty")
        void testCreateProduct_shouldThrowExceptionWhenSkuIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("");

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when description is null")
        void testCreateProduct_shouldThrowExceptionWhenDescriptionIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when description is empty")
        void testCreateProduct_shouldThrowExceptionWhenDescriptionIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when package type is null")
        void testCreateProduct_shouldThrowExceptionWhenPackageTypeIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType(null);

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when package type is empty")
        void testCreateProduct_shouldThrowExceptionWhenPackageTypeIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType("");

            assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        }

        // Unique fields

        @Test
        @DisplayName("Create product, should throw exception when sku already exists")
        void testCreateProduct_shouldThrowExceptionWhenSkuAlreadyExists() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("TEST-SKU");

            productService.createProduct(productDTO);

            assertThrows(ResourceAlreadyExistsException.class, () -> productService.createProduct(productDTO));
        }
    }

    @Nested
    @DisplayName("Get Product by Id")
    class GetProductByIdTest {

        @Test
        @DisplayName("Get product by id, should return product successfully")
        void testGetProductById_shouldReturnProductSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());

            Product foundProduct = productService.getProductById(createdProduct.getId());

            assertNotNull(foundProduct.getId());
            assertEquals(createdProduct.getId(), foundProduct.getId());
        }

        @Test
        @DisplayName("Get product by id, should throw exception when product not found")
        void testGetProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        }
    }

    @Nested
    @DisplayName("Delete Product by Id")
    class DeleteProductByIdTest {

        @Test
        @DisplayName("Delete product by id, should delete product successfully")
        void testDeleteProductById_shouldDeleteProductSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.deleteProductById(createdProduct.getId());

            Product deletedProduct = productRepository.findById(createdProduct.getId()).orElse(null);

            assertNotNull(deletedProduct);
            assertEquals(Status.DELETED, deletedProduct.getStatus());
        }

        @Test
        @DisplayName("Delete product by id, should throw exception when product not found")
        void testDeleteProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductById(1L));
        }

    }

    @Nested
    @DisplayName("Update Product by Id")
    class UpdateProductByIdTest {

        @Test
        @DisplayName("Update product by id, should update product successfully")
        void testUpdateProductById_shouldUpdateProductSuccessfully() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setStatus(Status.INACTIVE);

            Product createdProduct = productService.createProduct(productDTO);

            productDTO.setName("Test updated product");

            Product updatedProduct = productService.updateProductById(createdProduct.getId(), productDTO);

            assertNotNull(updatedProduct.getId());
            assertEquals(productDTO.getName(), updatedProduct.getName());
            assertEquals(productDTO.getDescription(), updatedProduct.getDescription());
        }

        @Test
        @DisplayName("Update product by id, should throw exception when product not found")
        void testUpdateProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.updateProductById(1L, getMockProductCreate()));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when name is null")
        void testUpdateProductById_shouldThrowExceptionWhenNameIsNull() {

            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when name is empty")
        void testUpdateProductById_shouldThrowExceptionWhenNameIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));

        }

        @Test
        @DisplayName("Update product by id, should throw exception when description is null")
        void testUpdateProductById_shouldThrowExceptionWhenDescriptionIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when description is empty")
        void testUpdateProductById_shouldThrowExceptionWhenDescriptionIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when package type is null")
        void testUpdateProductById_shouldThrowExceptionWhenPackageTypeIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType(null);

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when package type is empty")
        void testUpdateProductById_shouldThrowExceptionWhenPackageTypeIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType("");

            assertThrows(IllegalArgumentException.class, () -> productService.updateProductById(createdProduct.getId(), productDTO));
        }

    }

    @Nested
    @DisplayName("Patch Product by Id")
    class PatchProductByIdTest {

        @Test
        @DisplayName("Patch product by id, should patch product successfully")
        void testPatchProductById_shouldPatchProductSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("Test patched product");

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertNotNull(patchedProduct.getId());
            assertEquals(productDTO.getName(), patchedProduct.getName());
            assertEquals(productDTO.getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should throw exception when product not found")
        void testPatchProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.patchProductById(1L, getMockProductCreate()));
        }

        @Test
        @DisplayName("Patch product by id, should not update name when name is null")
        void testPatchProductById_shouldNotUpdateNameWhenNameIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName(null);

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getName(), patchedProduct.getName());
        }

        @Test
        @DisplayName("Patch product by id, should not update name when name is empty")
        void testPatchProductById_shouldNotUpdateNameWhenNameIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getName(), patchedProduct.getName());
        }

        @Test
        @DisplayName("Patch product by id, should not update description when description is null")
        void testPatchProductById_shouldNotUpdateDescriptionWhenDescriptionIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should not update description when description is empty")
        void testPatchProductById_shouldNotUpdateDescriptionWhenDescriptionIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should not update sku when sku is null")
        void testPatchProductById_shouldNotUpdateSkuWhenSkuIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku(null);

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getSku(), patchedProduct.getSku());
        }

        @Test
        @DisplayName("Patch product by id, should not update sku when sku is empty")
        void testPatchProductById_shouldNotUpdateSkuWhenSkuIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("");

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getSku(), patchedProduct.getSku());
        }

        @Test
        @DisplayName("Patch product by id, should not update family code when family code is null")
        void testPatchProductById_shouldNotUpdateFamilyCodeWhenFamilyCodeIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setFamilyCode(null);

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getFamilyCode(), patchedProduct.getFamilyCode());
        }

        @Test
        @DisplayName("Patch product by id, should not update family code when family code is empty")
        void testPatchProductById_shouldNotUpdateFamilyCodeWhenFamilyCodeIsEmpty() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setFamilyCode("");

            Product patchedProduct = productService.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getFamilyCode(), patchedProduct.getFamilyCode());
        }

    }

    @Nested
    @DisplayName("Add Product Price to Product by Id")
    class AddProductPriceToProductByIdTest {

        @Test
        @DisplayName("Add product price to product, should add product price successfully")
        void testAddProductPriceToProductById_shouldAddProductPriceSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();

            Product addedProductPrice = productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertNotNull(addedProductPrice.getId());
            assertEquals(BigDecimal.valueOf(productPriceDTO.getPrice()), addedProductPrice.getProductPrices().iterator().next().getPrice());
            assertEquals(productPriceDTO.getQuantity(), addedProductPrice.getProductPrices().iterator().next().getQuantity());
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when product not found")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.addProductPriceToProduct(1L, getMockProductPriceCreate()));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when price is null")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenPriceIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setPrice(null);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity is null")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(null);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity is negative")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityIsNegative() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(-1);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when price is negative")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenPriceIsNegative() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setPrice(-1.0);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity already exists")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityAlreadyExists() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);

            productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }
    }

    @Nested
    @DisplayName("Delete Product Price from Product by Id")
    class DeleteProductPriceFromProductByIdTest {

        @Test
        @DisplayName("Delete product price from product, should delete product price successfully")
        void testDeleteProductPriceFromProductById_shouldDeleteProductPriceSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());

            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);
            Product addedProductPrice = productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            productService.deleteProductPriceFromProduct(addedProductPrice.getId(), addedProductPrice.getProductPrices().iterator().next().getId());

            Product deletedProduct = productRepository.findById(createdProduct.getId()).orElse(null);

            assertNotNull(deletedProduct);
            assertTrue(deletedProduct.getProductPrices().isEmpty());
        }

        @Test
        @DisplayName("Delete product price from product, should throw exception when product not found")
        void testDeleteProductPriceFromProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductPriceFromProduct(1L, 1L));
        }

        @Test
        @DisplayName("Delete product price from product, should throw exception when price not found")
        void testDeleteProductPriceFromProductById_shouldThrowExceptionWhenPriceNotFound() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);

            productService.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertThrows(ResourceNotFoundException.class, () -> productService.deleteProductPriceFromProduct(createdProduct.getId(), 2L));
        }
    }

    @Nested
    @DisplayName("Add Product Stock to Product by Id")
    class AddProductStockToProductByIdTest {

        @Test
        @DisplayName("Add product stock to product, should add product stock successfully")
        void testAddProductStockToProductById_shouldAddProductStockSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();

            Product addedProductStock = productService.addProductStockToProduct(createdProduct.getId(), productStockDTO);

            assertNotNull(addedProductStock.getId());
            assertEquals(productStockDTO.getQuantity(), addedProductStock.getProductStock().getQuantity());
            assertEquals(0, addedProductStock.getProductStock().getReservedQuantity());
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when product not found")
        void testAddProductStockToProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.addProductStockToProduct(1L, getMockProductStockCreate()));
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when quantity is null")
        void testAddProductStockToProductById_shouldThrowExceptionWhenQuantityIsNull() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(null);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductStockToProduct(createdProduct.getId(), productStockDTO));
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when quantity is negative")
        void testAddProductStockToProductById_shouldThrowExceptionWhenQuantityIsNegative() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(-1);

            assertThrows(IllegalArgumentException.class, () -> productService.addProductStockToProduct(createdProduct.getId(), productStockDTO));
        }

        @Test
        @DisplayName("Add product stock to product, should add product stock successfully when quantity is zero")
        void testAddProductStockToProductById_shouldAddProductStockSuccessfullyWhenQuantityIsZero() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(0);

            Product addedProductStock = productService.addProductStockToProduct(createdProduct.getId(), productStockDTO);

            assertNotNull(addedProductStock.getId());
            assertEquals(productStockDTO.getQuantity(), addedProductStock.getProductStock().getQuantity());
            assertEquals(productStockDTO.getQuantity(), addedProductStock.getProductStock().getQuantity());
        }

    }

    @Nested
    @DisplayName("Get Product by Slug")
    class GetProductBySlugTest {

        @Test
        @DisplayName("Get product by slug, should return product successfully")
        void testGetProductBySlug_shouldReturnProductSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());

            Product foundProduct = productService.getProductBySlug(createdProduct.getSlug());

            assertNotNull(foundProduct.getId());
            assertEquals(createdProduct.getId(), foundProduct.getId());
        }

        @Test
        @DisplayName("Get product by slug, should throw exception when product not found")
        void testGetProductBySlug_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productService.getProductBySlug("test-slug"));
        }
    }

    @Nested
    @DisplayName("Search Products by Term")
    class SearchProductsByTermTest {

        @Test
        @DisplayName("Search products by term, should return products successfully")
        void testSearchProductsByTerm_shouldReturnProductsSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productService.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());

            Page<Product> foundProducts = productService.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(1, foundProducts.getContent().size());
            assertEquals(createdProduct.getId(), foundProducts.getContent().get(0).getId());
        }

        @Test
        @DisplayName("Search products by term, should not return deleted products")
        void testSearchProductsByTerm_shouldNotReturnDeletedProducts() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productService.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());
            productService.deleteProductById(createdProduct.getId());

            Page<Product> foundProducts = productService.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return inactive products")
        void testSearchProductsByTerm_shouldNotReturnInactiveProducts() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productService.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setStatus(Status.INACTIVE);
            productService.updateProductById(createdProduct.getId(), productDTO);

            Page<Product> foundProducts = productService.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return products without price")
        void testSearchProductsByTerm_shouldNotReturnProductsWithoutPrice() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());

            Page<Product> foundProducts = productService.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return products without stock")
        void testSearchProductsByTerm_shouldNotReturnProductsWithoutStock() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            productService.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());

            Page<Product> foundProducts = productService.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }
    }

    @Nested
    @DisplayName("Add Product Category to Product by Id")
    class AddProductCategoryToByIdTest {

        @Test
        @DisplayName("Add product category to product, should add product category successfully")
        void testAddProductCategoryToProductById_shouldAddProductCategorySuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            Category category = categoryRepository.save(getMockProductCategoryCreate());

            productService.addProductCategoryToProduct(createdProduct.getId(), category.getId());

            assertNotNull(createdProduct.getId());
            assertEquals(1, createdProduct.getProductCategories().size());
            assertEquals(category.getId(), createdProduct.getProductCategories().iterator().next().getId());
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when product not found")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenProductNotFound() {

            Category category = categoryRepository.save(getMockProductCategoryCreate());

            assertThrows(ResourceNotFoundException.class, () -> productService.addProductCategoryToProduct(1L, category.getId()));
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when category not found")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenCategoryNotFound() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            assertThrows(ResourceNotFoundException.class, () -> productService.addProductCategoryToProduct(createdProduct.getId(), 1L));
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when product already has category")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenProductAlreadyHasCategory() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            Category category = categoryRepository.save(getMockProductCategoryCreate());

            productService.addProductCategoryToProduct(createdProduct.getId(), category.getId());

            assertThrows(ResourceAlreadyExistsException.class, () -> productService.addProductCategoryToProduct(createdProduct.getId(), category.getId()));
        }

    }

    @Nested
    @DisplayName("Add Product Brand to Product by Id")
    class AddProductBrandToByIdTest {

        @Test
        @DisplayName("Add product brand to product, should add product brand successfully")
        void testAddProductBrandToProductById_shouldAddProductBrandSuccessfully() {

            Product createdProduct = productService.createProduct(getMockProductCreate());
            Brand brand = brandRepository.save(getMockProductBrandCreate());

            productService.addProductBrandToProduct(createdProduct.getId(), brand.getId());

            assertNotNull(createdProduct.getId());
            assertEquals(brand.getId(), createdProduct.getBrand().getId());
            assertEquals(brand.getName(), createdProduct.getBrand().getName());
        }

        @Test
        @DisplayName("Add product brand to product, should throw exception when product not found")
        void testAddProductBrandToProductById_shouldThrowExceptionWhenProductNotFound() {

            Brand brand = brandRepository.save(getMockProductBrandCreate());

            assertThrows(ResourceNotFoundException.class, () -> productService.addProductBrandToProduct(1L, brand.getId()));
        }

        @Test
        @DisplayName("Add product brand to product, should throw exception when brand not found")
        void testAddProductBrandToProductById_shouldThrowExceptionWhenBrandNotFound() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            assertThrows(ResourceNotFoundException.class, () -> productService.addProductBrandToProduct(createdProduct.getId(), 1L));
        }

        @Test
        @DisplayName("Add product brand to product, should not thow exception when product brand already exists")
        void testAddProductBrandToProductById_shouldNotThrowExceptionWhenProductBrandAlreadyExists() {
            Product createdProduct = productService.createProduct(getMockProductCreate());
            Brand brand = brandRepository.save(getMockProductBrandCreate());

            productService.addProductBrandToProduct(createdProduct.getId(), brand.getId());

            assertNotNull(createdProduct.getId());
            assertEquals(brand.getId(), createdProduct.getBrand().getId());
            assertEquals(brand.getName(), createdProduct.getBrand().getName());
        }

    }


}
