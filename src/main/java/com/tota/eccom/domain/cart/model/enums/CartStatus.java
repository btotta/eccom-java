package com.tota.eccom.domain.cart.model.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CartStatus {

    CART("CART"),
    PAID("PAID"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    CANCELED("CANCELED");

    private final String status;

    CartStatus(String status) {
        this.status = status;
    }

    public static CartStatus fromString(String status) {
        return Arrays.stream(CartStatus.values()).filter(s -> s.name().equals(status)).findFirst().orElse(null);
    }
}
