package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.common.enums.Status;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.product.repository.spec.ProductSpecification;
import com.tota.eccom.exceptions.product.ProductNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .orElseThrow(() -> new ProductNotFound("Product not found with given id: {}" + id));
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
    public Product addPriceToProduct(Long id, ProductCreatePrice productCreatePriceDTO) {

        Product product = getProductById(id);

        log.info("Adding price to product: {}", product);

        product.getPrices().add(productCreatePriceDTO.toProductPrice());

        return productRepository.save(product);
    }


}
