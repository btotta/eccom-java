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
import com.tota.eccom.domain.user.IUserService;
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
    private final IUserService userDomain;
    private final IProductDomain productDomain;



}
