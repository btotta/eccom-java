package com.tota.eccom.domain.cart.repository.spec;

import com.tota.eccom.domain.cart.model.Cart;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CartSpecification {

    public static Specification<Cart> filterCarts(Long userId) {
        return Specification.where(hasUserId(userId))
                .and(hasStatusNotDeleted())
                .and(hasStatusNotInactive())
                .and(notOlderThan(7))
                .and(orderByCreationDateDesc());
    }

    private static Specification<Cart> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    private static Specification<Cart> hasStatusNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("status"), "DELETED");
    }

    private static Specification<Cart> hasStatusNotInactive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("status"), "INACTIVE");
    }

    private static Specification<Cart> orderByCreationDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Cart> notOlderThan(int days) {
        LocalDateTime dateThreshold = LocalDateTime.now().minusDays(days);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), dateThreshold);
    }
}
