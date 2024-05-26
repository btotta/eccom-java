package com.tota.eccom.adapters.in;

import com.tota.eccom.domain.cart.ICartDomain;
import com.tota.eccom.domain.cart.model.Cart;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartDomain cartDomain;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get cart by user id or create a new one if it does not exist")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(cartDomain.getCartByUserId(userId), HttpStatus.OK);
    }
}
