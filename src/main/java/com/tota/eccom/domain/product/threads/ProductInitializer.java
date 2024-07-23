package com.tota.eccom.domain.product.threads;

import com.tota.eccom.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductInitializer {

    private final ProductRepository productRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void initializeRoles() {

        productRepository.deleteAll();

        productRepository.save(new InitialProductsDTOS().getCamiseta());
        productRepository.save(new InitialProductsDTOS().getBermuda());

    }

}
