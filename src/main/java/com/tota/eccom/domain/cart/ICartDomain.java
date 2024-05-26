package com.tota.eccom.domain.cart;

import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.stereotype.Component;

@Component
public interface ICartDomain {
    Cart getCartByUserId(Long userId);
}
