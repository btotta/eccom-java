package com.tota.eccom.domain.product.repository.spec;

import com.tota.eccom.domain.product.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> filterProducts(String name, String description, Double price, String brand,
                                                        String category) {
        return Specification.where(hasName(name))
                .and(hasDescription(description))
                .and(notDeleted())
                .and(notInactive())
                .and(hasBrand(brand))
                .and(hasCategory(category));
    }

    private static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> StringUtils.trimToEmpty(name).isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    private static Specification<Product> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> StringUtils.trimToEmpty(description).isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    private static Specification<Product> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), "DELETED");
    }

    private static Specification<Product> notInactive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), "INACTIVE");
    }

    private static Specification<Product> hasPrice(Double price) {
        return (root, query, criteriaBuilder) -> price == null ? criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("price"), price);
    }

    private static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> StringUtils.trimToEmpty(brand).isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get("brand"), "%" + brand + "%");
    }

    private static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> StringUtils.trimToEmpty(category).isEmpty() ? criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get("category"), "%" + category + "%");
    }

}
