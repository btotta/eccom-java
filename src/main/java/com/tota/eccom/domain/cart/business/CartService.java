package com.tota.eccom.domain.cart.business;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.domain.cart.ICartService;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.model.CartItem;
import com.tota.eccom.domain.cart.model.enums.CartStatus;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.cart.repository.spec.CartSpecification;
import com.tota.eccom.domain.product.IProductService;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.user.IUserService;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserService userDomain;
    private final IProductService productDomain;


    @Override
    @Transactional
    public Cart getCartByUser() {
        return getOrCreateCartByUserId(userDomain.getUserLogged())
                .orElseThrow(() -> new ResourceNotFoundException("Unable to get cart"));
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findByIdAndCartStatusAndUserId(id, CartStatus.CART, userDomain.getUserLogged().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with given id: " + id));
    }

    @Override
    @Transactional
    public void deleteCartById(Long id) {
        Cart cart = getCartById(id);
        cart.setStatus(Status.DELETED);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCartItemById(Long id, Long itemId) {
        Cart cart = getCartById(id);

        if (cart.getItems() == null || cart.getItems().stream().noneMatch(ci -> ci.getId().equals(itemId))) {
            throw new ResourceNotFoundException(String.format("Cart item with id %s not found", itemId));
        }

        cart.getItems().removeIf(ci -> ci.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCartById(Long id, CartItemReqDTO cartItemReqDTO) {
        Cart cart = getCartById(id);

        processCartItem(cartItemReqDTO, cart);
        sumCartItems(cart);

        return cartRepository.save(cart);
    }

    private Product getProductById(Long id) {
        return productDomain.getProductById(id);
    }

    private void processCartItem(CartItemReqDTO cartItemReqDTO, Cart cart) {

        Product product = getProductById(cartItemReqDTO.getProductId());

        if (cart.getItems() == null || cart.getItems().stream().noneMatch(ci -> ci.getProduct().getId().equals(product.getId()))) {
            addItemToCart(product, cartItemReqDTO.getQuantity(), cart);
            return;
        }

        if (cartItemReqDTO.getQuantity() == 0 || cartItemReqDTO.getQuantity() < 0) {
            log.info("Removing cart item id {}, product id {}", cart.getId(), product.getId());
            cart.getItems().removeIf(ci -> ci.getProduct().getId().equals(product.getId()));
            return;
        }

        cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresent(cartItem -> {
                    log.info("Updating cart item id {}, product id {}, quantity {}", cartItem.getId(), product.getId(), cartItemReqDTO.getQuantity());
                    cartItem.setQuantity(cartItemReqDTO.getQuantity());
                    cartItem.setPrice(getProductPriceByQuantity(product, cartItem.getQuantity()).getPrice());
                });
    }

    private ProductPrice getProductPriceByQuantity(Product product, Integer quantity) {
        return Optional.ofNullable(product.getProductPriceByQuantity(quantity))
                .orElseThrow(() -> {
                    log.error("Failed to add product to cart id {} with request {}", product.getId(), quantity);
                    return new IllegalArgumentException("Failed to add product to cart");
                });
    }

    private void addItemToCart(Product product, Integer quantity, Cart cart) {

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .price(getProductPriceByQuantity(product, quantity).getPrice())
                .build();

        log.info("Adding item to cart id {}, product id {}, quantity {}", cartItem.getId(), product.getId(), quantity);

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        cart.getItems().add(cartItemRepository.save(cartItem));
    }

    private void sumCartItems(Cart cart) {

        cart.getItems().forEach(ci -> ci.setPrice(getProductPriceByQuantity(ci.getProduct(), ci.getQuantity()).getPrice()));

        cart.setItemsCount(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum));

        cart.setTotalItems(cart.getItems().stream()
                .map(c -> c.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private Optional<Cart> getOrCreateCartByUserId(User user) {

        Optional<Cart> cart = cartRepository.findAll(
                        CartSpecification.findLastCarts(user.getId())).stream()
                .findFirst();

        if (cart.isEmpty()) {
            cart = Optional.of(Cart.builder()
                    .user(user)
                    .cartStatus(CartStatus.CART)
                    .totalItems(BigDecimal.ZERO)
                    .totalOrder(BigDecimal.ZERO)
                    .status(Status.ACTIVE)
                    .build());

            cartRepository.save(cart.get());
        }

        return cart;
    }
}
