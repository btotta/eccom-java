package com.tota.eccom.adapters.in;

import com.tota.eccom.domain.cart.ICartDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartDomain cartDomain;





}
