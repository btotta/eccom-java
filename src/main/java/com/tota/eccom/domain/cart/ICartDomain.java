package com.tota.eccom.domain.cart;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.stereotype.Component;

@Component
public interface ICartDomain {


    Cart addProductToCart(CartItemReqDTO cartItemReqDTO);
}
