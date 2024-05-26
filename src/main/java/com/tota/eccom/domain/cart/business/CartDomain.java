package com.tota.eccom.domain.cart.business;

import com.tota.eccom.domain.cart.ICartDomain;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.cart.repository.spec.CartSpecification;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.user.IUserDomain;
import com.tota.eccom.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartDomain implements ICartDomain {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserDomain userDomain;


    @Override
    public Cart getCartByUserId(Long userId) {

        Cart cart = getLastCartByUser(userDomain.getUserById(userId));

        if (cart == null) {
            cart = createCart(userId);
        }

        return cart;
    }

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
}
