package com.tota.eccom.adapters.dto.cart.response;

import com.tota.eccom.adapters.dto.product.response.ProductRespDTO;
import com.tota.eccom.domain.cart.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRespDTO {

    private Long id;
    private ProductRespDTO product;
    private Integer quantity;
    private BigDecimal price;


    public CartItemRespDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.product = new ProductRespDTO(cartItem.getProduct());
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }


}
