package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductCategory;
import com.tota.eccom.domain.product.repository.ProductCategoryRepository;
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
@Import({ProductDomain.class})
class ProductDomainTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductDomain productDomain;


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

    private ProductCategory getMockProductCategoryCreate() {
        return ProductCategory.builder()
                .name("Test Category")
                .description("Test Category Description")
                .slug(SlugUtil.makeSlug("Test Category"))
                .status(Status.ACTIVE)
                .build();
    }

    @Nested
    @DisplayName("Create Product")
    class CreateProductTest {

        @Test
        @DisplayName("Create product, should create product successfully")
        void testCreateProduct_shouldCreateProductSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());

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

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when name is empty")
        void testCreateProduct_shouldThrowExceptionWhenNameIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when sku is null")
        void testCreateProduct_shouldThrowExceptionWhenSkuIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when sku is empty")
        void testCreateProduct_shouldThrowExceptionWhenSkuIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when description is null")
        void testCreateProduct_shouldThrowExceptionWhenDescriptionIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when description is empty")
        void testCreateProduct_shouldThrowExceptionWhenDescriptionIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when package type is null")
        void testCreateProduct_shouldThrowExceptionWhenPackageTypeIsNull() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        @Test
        @DisplayName("Create product, should throw exception when package type is empty")
        void testCreateProduct_shouldThrowExceptionWhenPackageTypeIsEmpty() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.createProduct(productDTO));
        }

        // Unique fields

        @Test
        @DisplayName("Create product, should throw exception when sku already exists")
        void testCreateProduct_shouldThrowExceptionWhenSkuAlreadyExists() {
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("TEST-SKU");

            productDomain.createProduct(productDTO);

            assertThrows(ResourceAlreadyExistsException.class, () -> productDomain.createProduct(productDTO));
        }
    }

    @Nested
    @DisplayName("Get Product by Id")
    class GetProductByIdTest {

        @Test
        @DisplayName("Get product by id, should return product successfully")
        void testGetProductById_shouldReturnProductSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            Product foundProduct = productDomain.getProductById(createdProduct.getId());

            assertNotNull(foundProduct.getId());
            assertEquals(createdProduct.getId(), foundProduct.getId());
        }

        @Test
        @DisplayName("Get product by id, should throw exception when product not found")
        void testGetProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.getProductById(1L));
        }
    }

    @Nested
    @DisplayName("Delete Product by Id")
    class DeleteProductByIdTest {

        @Test
        @DisplayName("Delete product by id, should delete product successfully")
        void testDeleteProductById_shouldDeleteProductSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.deleteProductById(createdProduct.getId());

            Product deletedProduct = productRepository.findById(createdProduct.getId()).orElse(null);

            assertNotNull(deletedProduct);
            assertEquals(Status.DELETED, deletedProduct.getStatus());
        }

        @Test
        @DisplayName("Delete product by id, should throw exception when product not found")
        void testDeleteProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.deleteProductById(1L));
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

            Product createdProduct = productDomain.createProduct(productDTO);

            productDTO.setName("Test updated product");

            Product updatedProduct = productDomain.updateProductById(createdProduct.getId(), productDTO);

            assertNotNull(updatedProduct.getId());
            assertEquals(productDTO.getName(), updatedProduct.getName());
            assertEquals(productDTO.getDescription(), updatedProduct.getDescription());
        }

        @Test
        @DisplayName("Update product by id, should throw exception when product not found")
        void testUpdateProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.updateProductById(1L, getMockProductCreate()));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when name is null")
        void testUpdateProductById_shouldThrowExceptionWhenNameIsNull() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when name is empty")
        void testUpdateProductById_shouldThrowExceptionWhenNameIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));

        }

        @Test
        @DisplayName("Update product by id, should throw exception when description is null")
        void testUpdateProductById_shouldThrowExceptionWhenDescriptionIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when description is empty")
        void testUpdateProductById_shouldThrowExceptionWhenDescriptionIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when package type is null")
        void testUpdateProductById_shouldThrowExceptionWhenPackageTypeIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));
        }

        @Test
        @DisplayName("Update product by id, should throw exception when package type is empty")
        void testUpdateProductById_shouldThrowExceptionWhenPackageTypeIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductDTO productDTO = getMockProductCreate();
            productDTO.setPackageType("");

            assertThrows(IllegalArgumentException.class, () -> productDomain.updateProductById(createdProduct.getId(), productDTO));
        }

    }

    @Nested
    @DisplayName("Patch Product by Id")
    class PatchProductByIdTest {

        @Test
        @DisplayName("Patch product by id, should patch product successfully")
        void testPatchProductById_shouldPatchProductSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("Test patched product");

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertNotNull(patchedProduct.getId());
            assertEquals(productDTO.getName(), patchedProduct.getName());
            assertEquals(productDTO.getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should throw exception when product not found")
        void testPatchProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.patchProductById(1L, getMockProductCreate()));
        }

        @Test
        @DisplayName("Patch product by id, should not update name when name is null")
        void testPatchProductById_shouldNotUpdateNameWhenNameIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName(null);

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getName(), patchedProduct.getName());
        }

        @Test
        @DisplayName("Patch product by id, should not update name when name is empty")
        void testPatchProductById_shouldNotUpdateNameWhenNameIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setName("");

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getName(), patchedProduct.getName());
        }

        @Test
        @DisplayName("Patch product by id, should not update description when description is null")
        void testPatchProductById_shouldNotUpdateDescriptionWhenDescriptionIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription(null);

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should not update description when description is empty")
        void testPatchProductById_shouldNotUpdateDescriptionWhenDescriptionIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setDescription("");

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getDescription(), patchedProduct.getDescription());
        }

        @Test
        @DisplayName("Patch product by id, should not update sku when sku is null")
        void testPatchProductById_shouldNotUpdateSkuWhenSkuIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku(null);

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getSku(), patchedProduct.getSku());
        }

        @Test
        @DisplayName("Patch product by id, should not update sku when sku is empty")
        void testPatchProductById_shouldNotUpdateSkuWhenSkuIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setSku("");

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getSku(), patchedProduct.getSku());
        }

        @Test
        @DisplayName("Patch product by id, should not update family code when family code is null")
        void testPatchProductById_shouldNotUpdateFamilyCodeWhenFamilyCodeIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setFamilyCode(null);

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getFamilyCode(), patchedProduct.getFamilyCode());
        }

        @Test
        @DisplayName("Patch product by id, should not update family code when family code is empty")
        void testPatchProductById_shouldNotUpdateFamilyCodeWhenFamilyCodeIsEmpty() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setFamilyCode("");

            Product patchedProduct = productDomain.patchProductById(createdProduct.getId(), productDTO);

            assertEquals(getMockProductCreate().getFamilyCode(), patchedProduct.getFamilyCode());
        }

    }

    @Nested
    @DisplayName("Add Product Price to Product by Id")
    class AddProductPriceToProductByIdTest {

        @Test
        @DisplayName("Add product price to product, should add product price successfully")
        void testAddProductPriceToProductById_shouldAddProductPriceSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();

            Product addedProductPrice = productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertNotNull(addedProductPrice.getId());
            assertEquals(BigDecimal.valueOf(productPriceDTO.getPrice()), addedProductPrice.getProductPrices().iterator().next().getPrice());
            assertEquals(productPriceDTO.getQuantity(), addedProductPrice.getProductPrices().iterator().next().getQuantity());
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when product not found")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.addProductPriceToProduct(1L, getMockProductPriceCreate()));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when price is null")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenPriceIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setPrice(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity is null")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity is negative")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityIsNegative() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(-1);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when price is negative")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenPriceIsNegative() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setPrice(-1.0);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }

        @Test
        @DisplayName("Add product price to product, should throw exception when quantity already exists")
        void testAddProductPriceToProductById_shouldThrowExceptionWhenQuantityAlreadyExists() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);

            productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO));
        }
    }

    @Nested
    @DisplayName("Delete Product Price from Product by Id")
    class DeleteProductPriceFromProductByIdTest {

        @Test
        @DisplayName("Delete product price from product, should delete product price successfully")
        void testDeleteProductPriceFromProductById_shouldDeleteProductPriceSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);
            Product addedProductPrice = productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            productDomain.deleteProductPriceFromProduct(addedProductPrice.getId(), addedProductPrice.getProductPrices().iterator().next().getId());

            Product deletedProduct = productRepository.findById(createdProduct.getId()).orElse(null);

            assertNotNull(deletedProduct);
            assertTrue(deletedProduct.getProductPrices().isEmpty());
        }

        @Test
        @DisplayName("Delete product price from product, should throw exception when product not found")
        void testDeleteProductPriceFromProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.deleteProductPriceFromProduct(1L, 1L));
        }

        @Test
        @DisplayName("Delete product price from product, should throw exception when price not found")
        void testDeleteProductPriceFromProductById_shouldThrowExceptionWhenPriceNotFound() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductPriceDTO productPriceDTO = getMockProductPriceCreate();
            productPriceDTO.setQuantity(1);

            productDomain.addProductPriceToProduct(createdProduct.getId(), productPriceDTO);

            assertThrows(ResourceNotFoundException.class, () -> productDomain.deleteProductPriceFromProduct(createdProduct.getId(), 2L));
        }
    }

    @Nested
    @DisplayName("Add Product Stock to Product by Id")
    class AddProductStockToProductByIdTest {

        @Test
        @DisplayName("Add product stock to product, should add product stock successfully")
        void testAddProductStockToProductById_shouldAddProductStockSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();

            Product addedProductStock = productDomain.addProductStockToProduct(createdProduct.getId(), productStockDTO);

            assertNotNull(addedProductStock.getId());
            assertEquals(productStockDTO.getQuantity(), addedProductStock.getProductStock().getQuantity());
            assertEquals(0, addedProductStock.getProductStock().getReservedQuantity());
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when product not found")
        void testAddProductStockToProductById_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.addProductStockToProduct(1L, getMockProductStockCreate()));
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when quantity is null")
        void testAddProductStockToProductById_shouldThrowExceptionWhenQuantityIsNull() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(null);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductStockToProduct(createdProduct.getId(), productStockDTO));
        }

        @Test
        @DisplayName("Add product stock to product, should throw exception when quantity is negative")
        void testAddProductStockToProductById_shouldThrowExceptionWhenQuantityIsNegative() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(-1);

            assertThrows(IllegalArgumentException.class, () -> productDomain.addProductStockToProduct(createdProduct.getId(), productStockDTO));
        }

        @Test
        @DisplayName("Add product stock to product, should add product stock successfully when quantity is zero")
        void testAddProductStockToProductById_shouldAddProductStockSuccessfullyWhenQuantityIsZero() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductStockDTO productStockDTO = getMockProductStockCreate();
            productStockDTO.setQuantity(0);

            Product addedProductStock = productDomain.addProductStockToProduct(createdProduct.getId(), productStockDTO);

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

            Product createdProduct = productDomain.createProduct(getMockProductCreate());

            Product foundProduct = productDomain.getProductBySlug(createdProduct.getSlug());

            assertNotNull(foundProduct.getId());
            assertEquals(createdProduct.getId(), foundProduct.getId());
        }

        @Test
        @DisplayName("Get product by slug, should throw exception when product not found")
        void testGetProductBySlug_shouldThrowExceptionWhenProductNotFound() {
            assertThrows(ResourceNotFoundException.class, () -> productDomain.getProductBySlug("test-slug"));
        }
    }

    @Nested
    @DisplayName("Search Products by Term")
    class SearchProductsByTermTest {

        @Test
        @DisplayName("Search products by term, should return products successfully")
        void testSearchProductsByTerm_shouldReturnProductsSuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productDomain.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());

            Page<Product> foundProducts = productDomain.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(1, foundProducts.getContent().size());
            assertEquals(createdProduct.getId(), foundProducts.getContent().get(0).getId());
        }

        @Test
        @DisplayName("Search products by term, should not return deleted products")
        void testSearchProductsByTerm_shouldNotReturnDeletedProducts() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productDomain.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());
            productDomain.deleteProductById(createdProduct.getId());

            Page<Product> foundProducts = productDomain.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return inactive products")
        void testSearchProductsByTerm_shouldNotReturnInactiveProducts() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());
            productDomain.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());
            ProductDTO productDTO = getMockProductCreate();
            productDTO.setStatus(Status.INACTIVE);
            productDomain.updateProductById(createdProduct.getId(), productDTO);

            Page<Product> foundProducts = productDomain.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return products without price")
        void testSearchProductsByTerm_shouldNotReturnProductsWithoutPrice() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.addProductStockToProduct(createdProduct.getId(), getMockProductStockCreate());

            Page<Product> foundProducts = productDomain.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }

        @Test
        @DisplayName("Search products by term, should not return products without stock")
        void testSearchProductsByTerm_shouldNotReturnProductsWithoutStock() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            productDomain.addProductPriceToProduct(createdProduct.getId(), getMockProductPriceCreate());

            Page<Product> foundProducts = productDomain.searchProductsByTerm(getMockProductCreate().getName(), PageRequest.of(0, 10));

            assertNotNull(foundProducts.getContent());
            assertEquals(0, foundProducts.getContent().size());
        }
    }

    @Nested
    @DisplayName("Add Product Category to Product by Id")
    class AddProductCategoryToProductByIdTest {

        @Test
        @DisplayName("Add product category to product, should add product category successfully")
        void testAddProductCategoryToProductById_shouldAddProductCategorySuccessfully() {

            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductCategory productCategory = productCategoryRepository.save(getMockProductCategoryCreate());

            productDomain.addProductCategoryToProduct(createdProduct.getId(), productCategory.getId());

            assertNotNull(createdProduct.getId());
            assertEquals(1, createdProduct.getProductCategories().size());
            assertEquals(productCategory.getId(), createdProduct.getProductCategories().iterator().next().getId());
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when product not found")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenProductNotFound() {

            ProductCategory productCategory = productCategoryRepository.save(getMockProductCategoryCreate());

            assertThrows(ResourceNotFoundException.class, () -> productDomain.addProductCategoryToProduct(1L, productCategory.getId()));
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when category not found")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenCategoryNotFound() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            assertThrows(ResourceNotFoundException.class, () -> productDomain.addProductCategoryToProduct(createdProduct.getId(), 1L));
        }

        @Test
        @DisplayName("Add product category to product, should throw exception when product already has category")
        void testAddProductCategoryToProductById_shouldThrowExceptionWhenProductAlreadyHasCategory() {
            Product createdProduct = productDomain.createProduct(getMockProductCreate());
            ProductCategory productCategory = productCategoryRepository.save(getMockProductCategoryCreate());

            productDomain.addProductCategoryToProduct(createdProduct.getId(), productCategory.getId());

            assertThrows(ResourceAlreadyExistsException.class, () -> productDomain.addProductCategoryToProduct(createdProduct.getId(), productCategory.getId()));
        }

    }


}
