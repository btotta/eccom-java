package com.tota.eccom.adapters.in;

import com.tota.eccom.adapters.dto.cart.request.CartItemReqDTO;
import com.tota.eccom.adapters.dto.cart.response.CartRespDTO;
import com.tota.eccom.domain.cart.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints for cart management")
public class CartController {

    private final ICartService cartDomain;


    // Cart n Cart Item Operations
    @DeleteMapping("/{id}/item/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Delete cart item by id",
            description = "Deletes the cart item with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteCartItemById(@PathVariable Long id, @PathVariable Long itemId) {
        cartDomain.deleteCartItemById(id, itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Update cart by id",
            description = "Updates the cart with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<CartRespDTO> updateCartById(@PathVariable Long id, @RequestBody @Valid CartItemReqDTO cartItemReqDTO) {
        return new ResponseEntity<>(new CartRespDTO(cartDomain.updateCartById(id, cartItemReqDTO)), HttpStatus.OK);
    }


    // Cart Operations
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get cart by user",
            description = "Retrieves the cart with the specified user.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CartRespDTO> getCartByUser() {
        return new ResponseEntity<>(new CartRespDTO(cartDomain.getCartByUser()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get cart by id",
            description = "Retrieves the cart with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    public ResponseEntity<CartRespDTO> getCartById(@PathVariable Long id) {
        return new ResponseEntity<>(new CartRespDTO(cartDomain.getCartById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Delete cart by id",
            description = "Deletes the cart with the specified ID.",
            security = @SecurityRequirement(name = "Authorization")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteCartById(@PathVariable Long id) {
        cartDomain.deleteCartById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
