package com.tota.eccom.adapters.dto.cart.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartAddItemsReq {

    List<CartItemReq> items;

}
