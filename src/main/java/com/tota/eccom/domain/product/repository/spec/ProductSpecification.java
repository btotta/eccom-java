package com.tota.eccom.domain.product.repository.spec;

import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.product.model.ProductStock;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> searchProductsByTerm(String term) {

        return Specification.where(onName(term))
                .or(onDescription(term))
                .and(notDeleted())
                .and(notInactive())
                .and(hasPrice())
                .and(hasAvailableStock())
                .and(isDistinct());
    }

    private static Specification<Product> isDistinct() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Product> onName(String term) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + term.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Product> onDescription(String term) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + term.toLowerCase() + "%"
            );
        };
    }

    private static Specification<Product> hasPrice() {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductPrice> productPriceJoin = root.join("productPrices", JoinType.LEFT);
            return criteriaBuilder.and(
                    criteriaBuilder.isNotEmpty(root.get("productPrices")),
                    criteriaBuilder.equal(productPriceJoin.get("quantity"), 1)
            );
        };
    }

    private static Specification<Product> hasAvailableStock() {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductStock> productStockJoin = root.join("productStock", JoinType.LEFT);
            return criteriaBuilder.greaterThan(
                    criteriaBuilder.diff(productStockJoin.get("quantity"), productStockJoin.get("reservedQuantity")),
                    0
            );
        };
    }

    private static Specification<Product> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), "DELETED");
    }

    private static Specification<Product> notInactive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), "INACTIVE");
    }
}
