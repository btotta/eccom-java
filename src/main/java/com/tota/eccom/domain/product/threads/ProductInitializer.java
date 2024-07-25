package com.tota.eccom.domain.product.threads;

import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductBrand;
import com.tota.eccom.domain.product.model.ProductCategory;
import com.tota.eccom.domain.product.repository.ProductBrandRepository;
import com.tota.eccom.domain.product.repository.ProductCategoryRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductInitializer {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductBrandRepository productBrandRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void initializeProducts() {

        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        productBrandRepository.deleteAll();

        ProductBrand brand = productBrandRepository.save(new InitialProductsDTOS().getBrand());
        log.info("Brand created on initialize products: {}", brand);
        ProductCategory category = productCategoryRepository.save(new InitialProductsDTOS().getCategory());
        log.info("Category created on initialize products: {}", category);

        Product camiseta = new InitialProductsDTOS().getCamiseta();
        camiseta.setProductBrand(brand);
        camiseta.setProductCategories(List.of(category));
        productRepository.save(camiseta);
        log.info("Product camiseta created on initialize products: {}", camiseta);

        Product bermuda = new InitialProductsDTOS().getBermuda();
        bermuda.setProductBrand(brand);
        bermuda.setProductCategories(List.of(category));
        productRepository.save(bermuda);
        log.info("Product bermuda created on initialize products: {}", bermuda);
    }
}
