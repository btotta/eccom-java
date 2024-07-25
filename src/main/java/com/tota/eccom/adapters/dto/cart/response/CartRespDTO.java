package com.tota.eccom.adapters.dto.cart.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tota.eccom.adapters.dto.user.response.UserRespDTO;
import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.model.CartItem;
import com.tota.eccom.domain.cart.model.enums.CartStatus;
import com.tota.eccom.util.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRespDTO {

    private Long id;
    private UserRespDTO user;
    private CartStatus cartStatus;
    private BigDecimal totalItems;
    private Integer itemsCount;
    private BigDecimal totalOrder;
    private List<CartItemRespDTO> items;
    private Date createdAt;
    private Date updatedAt;
    private Status status;

    public CartRespDTO(Cart cart) {
        this.id = cart.getId();
        this.user = new UserRespDTO(cart.getUser());
        this.cartStatus = cart.getCartStatus();
        this.totalItems = cart.getTotalItems();
        this.itemsCount = cart.getItemsCount();
        this.totalOrder = cart.getTotalOrder();
        this.items = cart.getItems().stream().map(CartItemRespDTO::new).toList();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getUpdatedAt();
        this.status = cart.getStatus();
    }


}
