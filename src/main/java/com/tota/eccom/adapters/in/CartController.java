package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.cart.request.CartAddItemsReq;
import com.tota.eccom.adapters.dto.cart.request.CartItemReq;
import com.tota.eccom.adapters.dto.exception.ErrorDetails;
import com.tota.eccom.domain.cart.ICartDomain;
import com.tota.eccom.domain.cart.model.Cart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartDomain cartDomain;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    @Operation(
            summary = "Get cart by user id or create a new one if it does not exist",
            description = "Retrieves the cart with the specified user ID or creates a new one if it does not exist.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart retrieved successfully", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<Cart> getCartByLoggedUser() {
        return new ResponseEntity<>(cartDomain.getCartByLoggedUser(), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/add/{cartId}/")
    @Operation(
            summary = "Add product to cart",
            description = "Adds the product with the specified ID to the cart.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product added to cart successfully", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @RequestBody CartAddItemsReq cartAddItemsReq) {
        return new ResponseEntity<>(cartDomain.addProductToCart(cartId, cartAddItemsReq), HttpStatus.OK);
    }
}
