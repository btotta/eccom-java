package com.tota.eccom.domain.cart.repository;

import com.tota.eccom.domain.cart.model.Cart;
import com.tota.eccom.domain.cart.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {

    Optional<Cart> findByIdAndCartStatusAndUserId(Long id, CartStatus cartStatus, Long id1);
}
