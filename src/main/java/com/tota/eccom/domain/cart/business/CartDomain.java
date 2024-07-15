package com.tota.eccom.domain.cart.business;

import com.tota.eccom.adapters.dto.cart.request.CartAddItemsReq;
import com.tota.eccom.adapters.dto.cart.request.CartItemReq;
import com.tota.eccom.domain.cart.ICartDomain;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.model.CartItem;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.cart.repository.spec.CartSpecification;
import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import com.tota.eccom.exceptions.cart.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartDomain implements ICartDomain {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserDomain userDomain;
    private final IProductDomain productDomain;


    private Cart createCart(Long userId) {

        log.info("Creating cart for user with id: {}", userId);

        User user = userDomain.getUserById(userId);

        Cart cart = Cart.builder()
                .user(user)
                .status(Status.ACTIVE)
                .build();

        return cartRepository.save(cart);
    }

    private Cart getLastCartByUser(User user) {
        return cartRepository.findAll(CartSpecification.filterCarts(user.getId())).stream().findFirst().orElse(null);
    }

    @Override
    public Cart getCartByLoggedUser() {

        User user = userDomain.getUserLogged();

        Cart cart = getLastCartByUser(user);

        if (cart == null) {
            cart = createCart(user.getId());
        }

        return cart;
    }

    private Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));
    }

    @Override
    public Cart addProductToCart(Long cartId, CartAddItemsReq cartAddItemsReq) {

        Cart cart = getCartById(cartId);


        log.info("Adding to cart with id: {} products with ids: {}", cart.getId(),
                String.join(",", cartAddItemsReq.getItems().stream().map(CartItemReq::getProductId).map(String::valueOf).toList()));

        cartAddItemsReq.getItems().forEach(item -> sumProductToCart(cart, item));

        totalizatorItems(cart);

        return cart;
    }

    private void sumProductToCart(Cart cart, CartItemReq item) {

        cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId().equals(item.getProductId())).findFirst().ifPresentOrElse(
                cartItem -> {
                    if (cartItem.getQuantity() + item.getQuantity() <= 0) {
                        cartItemRepository.delete(cartItem);
                    } else {
                        cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                        cartItemRepository.save(cartItem);
                    }
                },
                () -> cartItemRepository.save(CartItem.builder()
                        .cart(cart)
                        .product(productDomain.getProductById(item.getProductId()))
                        .quantity(item.getQuantity())
                        .build())
        );
    }

    private void totalizatorItems(Cart cart) {
        cart.getItems().forEach(cartItem -> {

            if (cartItem.getQuantity() <= 0) {
                cartItemRepository.delete(cartItem);
                return;
            }

            BigDecimal price = productDomain.getProductBestPrice(cartItem.getProduct().getId(), cartItem.getQuantity());
            cartItem.setPrice(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())).setScale(2, RoundingMode.HALF_UP));
            cartItemRepository.save(cartItem);
        });
    }
}
