package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.adapters.dto.cart.response.CartRespDTO;
import com.tota.eccom.domain.cart.ICartDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartDomain cartDomain;


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the cart with the provided details.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartRespDTO> addProductToCart(@RequestBody @Valid CartItemReqDTO cartItemReqDTO) {
        return new ResponseEntity<>(new CartRespDTO(cartDomain.addProductToCart(cartItemReqDTO)), HttpStatus.OK);
    }



}
