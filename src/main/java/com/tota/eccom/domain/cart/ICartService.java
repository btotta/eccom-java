package com.tota.eccom.domain.cart;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.stereotype.Component;

@Component
public interface ICartService {

    Cart getCartByUser();

    Cart getCartById(Long id);

    void deleteCartById(Long id);

    void deleteCartItemById(Long id, Long itemId);

    Cart updateCartById(Long id, CartItemReqDTO cartItemReqDTO);
}
