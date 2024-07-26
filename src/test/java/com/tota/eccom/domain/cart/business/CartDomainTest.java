package com.tota.eccom.domain.cart.business;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.product.business.ProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.product.model.ProductStock;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.domain.category.repository.CategoryRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.user.business.UserService;
import com.tota.eccom.domain.user.model.Role;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.domain.user.repository.RoleRepository;
import com.tota.eccom.domain.user.repository.UserRepository;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.exceptions.user.UserNotFoundException;
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
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

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
        categoryRepository.deleteAll();
        brandRepository.deleteAll();
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
    @DisplayName("Get Cart by User")
    class GetCartByUserTest {

        @Test
        @DisplayName("Get cart by user, should return cart successfully")
        void testGetCartByUser_shouldReturnCartSuccessfully() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Cart cart = cartDomain.getCartByUser();

            assertNotNull(cart.getId());
            assertEquals(user.getId(), cart.getUser().getId());
        }

        @Test
        @DisplayName("Get cart by user, should throw exception when user not logged")
        void testGetCartByUser_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> cartDomain.getCartByUser());
        }
    }


    @Nested
    @DisplayName("Get Cart by Id")
    class GetCartByIdTest {

        @Test
        @DisplayName("Get cart by id, should return cart successfully")
        void testGetCartById_shouldReturnCartSuccessfully() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Cart cart = cartDomain.getCartByUser();

            Cart foundCart = cartDomain.getCartById(cart.getId());

            assertNotNull(foundCart.getId());
            assertEquals(cart.getId(), foundCart.getId());
            assertEquals(cart.getUser().getId(), foundCart.getUser().getId());
        }

        @Test
        @DisplayName("Get cart by id, should throw exception when cart not found")
        void testGetCartById_shouldThrowExceptionWhenCartNotFound() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            assertThrows(ResourceNotFoundException.class, () -> cartDomain.getCartById(1L));
        }

        @Test
        @DisplayName("Get cart by id, should throw exception when user not logged")
        void testGetCartById_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> cartDomain.getCartById(1L));
        }

    }

    @Nested
    @DisplayName("Delete Cart by Id")
    class DeleteCartByIdTest {

        @Test
        @DisplayName("Delete cart by id, should delete cart successfully")
        void testDeleteCartById_shouldDeleteCartSuccessfully() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Cart cart = cartDomain.getCartByUser();

            cartDomain.deleteCartById(cart.getId());

            Cart deletedCart = cartRepository.findById(cart.getId()).orElse(null);

            assertNotNull(deletedCart);
            assertEquals(Status.DELETED, deletedCart.getStatus());
        }

        @Test
        @DisplayName("Delete cart by id, should throw exception when cart not found")
        void testDeleteCartById_shouldThrowExceptionWhenCartNotFound() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            assertThrows(ResourceNotFoundException.class, () -> cartDomain.deleteCartById(1L));
        }

        @Test
        @DisplayName("Delete cart by id, should throw exception when user not logged")
        void testDeleteCartById_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> cartDomain.deleteCartById(1L));
        }
    }

    @Nested
    @DisplayName("Delete Cart Item by Id")
    class DeleteCartItemByIdTest {

        @Test
        @DisplayName("Delete cart item by id, should delete cart item successfully")
        void testDeleteCartItemById_shouldDeleteCartItemSuccessfully() {

            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Product product = getMockProduct();
            product.setProductStock(getMockProductStock());
            product.setProductPrices(List.of(getMockProductPrice()));
            productRepository.save(product);

            Cart cart = cartDomain.getCartByUser();

            Cart addedCartItem = cartDomain.updateCartById(cart.getId(), CartItemReqDTO.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build());

            cartDomain.deleteCartItemById(addedCartItem.getId(), addedCartItem.getItems().get(0).getId());

            Cart deletedCartItem = cartRepository.findById(addedCartItem.getId()).orElse(null);

            assertNotNull(deletedCartItem);
            assertEquals(0, deletedCartItem.getItems().size());
        }

        @Test
        @DisplayName("Delete cart item by id, should throw exception when cart not found")
        void testDeleteCartItemById_shouldThrowExceptionWhenCartNotFound() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            assertThrows(ResourceNotFoundException.class, () -> cartDomain.deleteCartItemById(1L, 1L));
        }

        @Test
        @DisplayName("Delete cart item by id, should throw exception when user not logged")
        void testDeleteCartItemById_shouldThrowExceptionWhenUserNotLogged() {
            assertThrows(UserNotFoundException.class, () -> cartDomain.deleteCartItemById(1L, 1L));
        }

        @Test
        @DisplayName("Delete cart item by id, should throw exception when cart item not found")
        void testDeleteCartItemById_shouldThrowExceptionWhenCartItemNotFound() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Product product = getMockProduct();
            product.setProductStock(getMockProductStock());
            product.setProductPrices(List.of(getMockProductPrice()));
            productRepository.save(product);

            Cart cart = cartDomain.getCartByUser();

            assertThrows(ResourceNotFoundException.class, () -> cartDomain.deleteCartItemById(cart.getId(), 1L));
        }
    }


    @Nested
    @DisplayName("Update Cart by Id")
    class UpdateCartByIdTest {

        @Test
        @DisplayName("Update cart by id, should update cart successfully")
        void testUpdateCartById_shouldUpdateCartSuccessfully() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Product product = getMockProduct();
            product.setProductStock(getMockProductStock());
            product.setProductPrices(List.of(getMockProductPrice()));
            productRepository.save(product);

            Cart cart = cartDomain.getCartByUser();

            CartItemReqDTO req = CartItemReqDTO.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build();

            Cart updatedCart = cartDomain.updateCartById(cart.getId(), req);

            assertNotNull(updatedCart.getId());
            assertEquals(1, updatedCart.getItems().size());
            assertEquals(product.getId(), updatedCart.getItems().get(0).getProduct().getId());
        }

        @Test
        @DisplayName("Update cart by id, should throw exception when cart not found")
        void testUpdateCartById_shouldThrowExceptionWhenCartNotFound() {
            User user = setupUser();

            when(securityUtil.getCurrentUsername()).thenReturn(user.getEmail());

            Product product = getMockProduct();
            product.setProductStock(getMockProductStock());
            product.setProductPrices(List.of(getMockProductPrice()));
            productRepository.save(product);

            assertThrows(ResourceNotFoundException.class, () -> cartDomain.updateCartById(2L, CartItemReqDTO.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build()));
        }

        @Test
        @DisplayName("Update cart by id, should throw exception when user not logged")
        void testUpdateCartById_shouldThrowExceptionWhenUserNotLogged() {

            assertThrows(UserNotFoundException.class, () -> cartDomain.updateCartById(1L, CartItemReqDTO.builder()
                    .productId(1L)
                    .quantity(1)
                    .build()));
        }
    }

}