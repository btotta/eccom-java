package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.exceptions.product.ProductNotFoundException;
import com.tota.eccom.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({ProductDomain.class, JwtTokenUtil.class})
class ProductDomainTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDomain productDomain;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private ProductCreate productCreateDTO;
    private ProductUpdate productUpdateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productCreateDTO = mockProductCreateDTO();
        productUpdateDTO = mockProductUpdateDTO();

    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    private ProductUpdate mockProductUpdateDTO() {
        return ProductUpdate.builder()
                .name("Test Product")
                .description("Test Product Description")
                .brand("Test Brand")
                .category("Test Category")
                .sku("Test SKU")
                .build();
    }


    private ProductCreate mockProductCreateDTO() {
        return ProductCreate.builder()
                .name("Test Product")
                .description("Test Product Description")
                .brand("Test Brand")
                .category("Test Category")
                .sku("Test SKU")
                .build();
    }


    @Nested
    @DisplayName("Create Product")
    class CreateProduct {

        @Test
        @DisplayName("When a product is created, then it should return the created product")
        void createProduct() {
            Product product = productDomain.createProduct(productCreateDTO);

            assertNotNull(product);
            assertEquals(productCreateDTO.getName(), product.getName());
            assertEquals(productCreateDTO.getDescription(), product.getDescription());
            assertEquals(productCreateDTO.getBrand(), product.getBrand());
            Assertions.assertEquals(productCreateDTO.getCategory(), product.getCategory());
            assertEquals(productCreateDTO.getSku(), product.getSku());
        }


        @Test
        @DisplayName("When a product is created with price, then it should return the created product")
        void createProductWithPrice() {

            productCreateDTO.setPrices(List.of(
                    ProductCreatePrice.builder()
                            .quantity(1)
                            .price(new BigDecimal(100))
                            .build())
            );

            Product product = productDomain.createProduct(productCreateDTO);

            assertNotNull(product);
            assertEquals(productCreateDTO.getName(), product.getName());
            assertEquals(productCreateDTO.getDescription(), product.getDescription());
            assertEquals(productCreateDTO.getBrand(), product.getBrand());
            assertEquals(productCreateDTO.getCategory(), product.getCategory());
            assertEquals(productCreateDTO.getSku(), product.getSku());
            assertEquals(productCreateDTO.getPrices().get(0).getPrice(), product.getPrices().get(0).getPrice());
        }

    }

    @Nested
    @DisplayName("Update Product")
    class UpdateProduct {

        @Test
        @DisplayName("When a product is updated, then it should return the updated product")
        void updateProduct() {
            Product product = productDomain.createProduct(productCreateDTO);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);

            assertNotNull(updatedProduct);
            assertEquals(productUpdateDTO.getName(), updatedProduct.getName());
            assertEquals(productUpdateDTO.getDescription(), updatedProduct.getDescription());
            assertEquals(productUpdateDTO.getBrand(), updatedProduct.getBrand());
            assertEquals(productUpdateDTO.getCategory(), updatedProduct.getCategory());
            assertEquals(productUpdateDTO.getSku(), updatedProduct.getSku());
        }

        @Test
        @DisplayName("When a product is updated with a null brand, should not update the brand")
        void updateProductWithNullBrand() {
            Product product = productDomain.createProduct(productCreateDTO);
            productUpdateDTO.setBrand(null);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);
            assertEquals(product.getBrand(), updatedProduct.getBrand());
        }

        @Test
        @DisplayName("When a product is updated with a null category, should not update the category")
        void updateProductWithNullCategory() {
            Product product = productDomain.createProduct(productCreateDTO);
            productUpdateDTO.setCategory(null);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);
            assertEquals(product.getCategory(), updatedProduct.getCategory());
        }

        @Test
        @DisplayName("When a product is updated with a null sku, should not update the sku")
        void updateProductWithNullSku() {
            Product product = productDomain.createProduct(productCreateDTO);
            productUpdateDTO.setSku(null);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);
            assertEquals(product.getSku(), updatedProduct.getSku());
        }

        @Test
        @DisplayName("When a product is updated with a null name, should not update the name")
        void updateProductWithNullName() {
            Product product = productDomain.createProduct(productCreateDTO);
            productUpdateDTO.setName(null);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);
            assertEquals(product.getName(), updatedProduct.getName());
        }

        @Test
        @DisplayName("When a product is updated with a null description, should not update the description")
        void updateProductWithNullDescription() {
            Product product = productDomain.createProduct(productCreateDTO);
            productUpdateDTO.setDescription(null);

            Product updatedProduct = productDomain.updateProductById(product.getId(), productUpdateDTO);
            assertEquals(product.getDescription(), updatedProduct.getDescription());
        }

    }

    @Nested
    @DisplayName("Delete Product -- Soft Delete")
    class DeleteProduct {

        @Test
        @DisplayName("When a product does not exist, then it should throw an exception")
        void deleteProductWithNonExistingProduct() {
            assertThrows(ProductNotFoundException.class, () -> productDomain.deleteProductById(1L));
        }

        @Test
        @DisplayName("When a product is deleted, then it should return the deleted product")
        void deleteProduct() {
            Product product = productDomain.createProduct(productCreateDTO);

            productDomain.deleteProductById(product.getId());

            Product deletedProduct = productRepository.findById(product.getId()).orElse(null);

            assertNotNull(deletedProduct);
            assertEquals(product.getId(), deletedProduct.getId());
            assertEquals(product.getName(), deletedProduct.getName());
            assertEquals(Status.DELETED, deletedProduct.getStatus());
        }

    }

    @Nested
    @DisplayName("Get Product")
    class GetProduct {

        @Test
        @DisplayName("When a product does not exist, then it should throw an exception")
        void getProductWithNonExistingProduct() {
            assertThrows(ProductNotFoundException.class, () -> productDomain.getProductById(1L));
        }

        @Test
        @DisplayName("When a product exists, then it should return the product")
        void getProduct() {
            Product product = productDomain.createProduct(productCreateDTO);

            Product foundProduct = productDomain.getProductById(product.getId());

            assertNotNull(foundProduct);
            assertEquals(product.getId(), foundProduct.getId());
            assertEquals(product.getName(), foundProduct.getName());
        }

    }


}
