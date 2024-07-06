package com.tota.eccom.domain.cart;

import com.tota.eccom.adapters.dto.cart.request.CartAddItemsReq;
import com.tota.eccom.adapters.dto.cart.request.CartItemReq;
import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.stereotype.Component;

@Component
public interface ICartDomain {

    Cart getCartByLoggedUser();

    Cart addProductToCart(Long cartId, CartAddItemsReq cartAddItemsReq);
}
