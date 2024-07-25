package com.tota.eccom.domain.cart.repository.spec;

import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CartSpecification {

    public static Specification<Cart> findLastCarts(Long userId) {
        return Specification.where(hasUserId(userId))
                .and(hasStatusActive())
                .and(notOlderThan(7))
                .and(orderByCreationDateDesc())
                .and(isACart());
    }

    private static Specification<Cart> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    private static Specification<Cart> hasStatusActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), "ACTIVE");
    }

    private static Specification<Cart> orderByCreationDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updatedAt")));
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Cart> notOlderThan(int days) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(days);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), dateThreshold);
    }

    private static Specification<Cart> isACart() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cartStatus"), "CART");
    }
}
