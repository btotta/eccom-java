package com.tota.eccom.domain.cart.business;

import com.tota.eccom.domain.cart.ICartDomain;
import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.user.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartDomain implements ICartDomain {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserService userDomain;
    private final IProductDomain productDomain;



}
