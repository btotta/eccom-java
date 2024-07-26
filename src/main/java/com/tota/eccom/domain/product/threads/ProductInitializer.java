package com.tota.eccom.domain.product.threads;

import com.tota.eccom.domain.cart.repository.CartItemRepository;
import com.tota.eccom.domain.cart.repository.CartRepository;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.domain.category.model.Category;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.domain.category.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void initializeProducts() {

        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        brandRepository.deleteAll();

        Brand brand = brandRepository.save(new InitialProductsDTOS().getBrand());
        log.info("Brand created on initialize products: {}", brand);
        Category category = categoryRepository.save(new InitialProductsDTOS().getCategory());
        log.info("Category created on initialize products: {}", category);

        Product camiseta = new InitialProductsDTOS().getCamiseta();
        camiseta.setBrand(brand);
        camiseta.setProductCategories(List.of(category));
        productRepository.save(camiseta);
        log.info("Product camiseta created on initialize products: {}", camiseta);

        Product bermuda = new InitialProductsDTOS().getBermuda();
        bermuda.setBrand(brand);
        bermuda.setProductCategories(List.of(category));
        productRepository.save(bermuda);
        log.info("Product bermuda created on initialize products: {}", bermuda);
    }
}
