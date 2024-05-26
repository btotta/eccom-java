package com.tota.eccom.domain.cart.repository;

import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {

    Cart findByUserId(Long userId);
}
