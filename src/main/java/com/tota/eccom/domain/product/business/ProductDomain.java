package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPrice;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.product.repository.spec.ProductSpecification;
import com.tota.eccom.exceptions.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductDomain implements IProductDomain {

    private final ProductRepository productRepository;


    @Override
    public Product createProduct(ProductCreate productCreateDTO) {
        Product product = productCreateDTO.toProduct();

        log.info("Creating product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {

        log.info("Getting product by id: {}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with given id: {}" + id));
    }

    @Override
    public void deleteProductById(Long id) {

        Product product = getProductById(id);

        log.info("Deleting product by id: {}", id);

        product.setStatus(Status.DELETED);
        product.setCreatedAt(LocalDateTime.now());

        productRepository.save(product);
    }

    @Override
    public Product updateProductById(Long id, ProductUpdate productUpdateDTO) {

        Product product = getProductById(id);

        log.info("Updating product: {}", product);

        productUpdateDTO.updateProduct(product);

        return productRepository.save(product);
    }

    @Override
    public Page<Product> getAllProductsPaginated(Pageable pageable, String name, String description, Double price, String brand, String category) {

        return productRepository.findAll(ProductSpecification.filterProducts(name, description, price, brand, category), pageable);
    }

    @Override
    public void deletePriceFromProduct(Long id, Long idPrice) {

        Product product = getProductById(id);

        log.info("Deleting price from product: {}", product);

        if (Objects.nonNull(product.getPrices()) && product.getPrices().removeIf(productPrice -> productPrice.getId().equals(idPrice))) {
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Price not found with given id: {}" + idPrice);
        }
    }

    @Override
    public BigDecimal getProductBestPrice(Long id, Integer quantity) {

        Product product = getProductById(id);

        log.info("Getting best price for product: {}", product);

        return product.getPrices().stream()
                .filter(productPrice -> productPrice.getQuantity() <= quantity)
                .map(ProductPrice::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Product addPriceToProduct(Long id, ProductCreatePrice productCreatePriceDTO) {

        Product product = getProductById(id);

        log.info("Adding price to product: {}", product);

        product.getPrices().add(productCreatePriceDTO.toProductPrice());

        return productRepository.save(product);
    }


}
