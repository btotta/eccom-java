package com.tota.eccom.domain.cart.business;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.product.business.ProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.product.model.ProductStock;
import com.tota.eccom.domain.product.repository.ProductBrandRepository;
import com.tota.eccom.domain.product.repository.ProductCategoryRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.user.business.UserService;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.RoleRepository;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.JwtTokenUtil;
import com.tota.eccom.util.SecurityUtil;
import com.tota.eccom.util.enums.Status;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({CartDomain.class, UserService.class, ProductDomain.class, JwtTokenUtil.class, SecurityUtil.class})
class CartDomainTest {


    @Autowired
    CartDomain cartDomain;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Autowired
    ProductBrandRepository productBrandRepository;

    @Autowired
    RoleRepository roleRepository;

    @MockBean
    private SecurityUtil securityUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        productBrandRepository.deleteAll();
    }


    private Product getMockProduct() {
        return Product.builder()
                .name("Test Product")
                .slug("test-product")
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
                .status(Status.ACTIVE)
                .build();
    }

    private ProductStock getMockProductStock() {
        return ProductStock.builder()
                .quantity(1000)
                .reservedQuantity(10)
                .build();
    }

    private ProductPrice getMockProductPrice() {
        return ProductPrice.builder()
                .price(BigDecimal.valueOf(20F))
                .quantity(1)
                .build();
    }

    private User setupUser() {
        Role userRole = Role.builder()
                .name("USER")
                .status(Status.ACTIVE)
                .build();

        roleRepository.save(userRole);

        User user = User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("v*cb592K6shz@5gr")
                .roles(new HashSet<>())
                .status(Status.ACTIVE)
                .build();

        user.getRoles().add(userRole);
        userRepository.save(user);

        return user;
    }


    @Nested
    @DisplayName("Add Product to Cart")
    class AddProductToCartTest {

        @Test
        @DisplayName("Add product to cart, should add product to cart successfully")
        void testAddProductToCart_shouldAddProductToCartSuccessfully() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Product product = getMockProduct();
            product.setProductStock(getMockProductStock());
            product.setProductPrices(List.of(getMockProductPrice()));
            productRepository.save(product);

            CartItemReqDTO req = CartItemReqDTO.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build();

            Cart cart = cartDomain.addProductToCart(req);

            assertNotNull(cart.getId());
            assertEquals(1, cart.getItems().size());
            assertEquals(product.getId(), cart.getItems().get(0).getProduct().getId());
        }

        @Test
        @DisplayName("Add product to cart, should throw exception when product not found")
        void testAddProductToCart_shouldThrowExceptionWhenProductNotFound() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            assertThrows(ResourceNotFoundException.class, () -> {
                cartDomain.addProductToCart(CartItemReqDTO.builder()
                        .productId(1L)
                        .quantity(1)
                        .build());
            });
        }
    }


}