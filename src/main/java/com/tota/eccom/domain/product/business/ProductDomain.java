package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.product.repository.spec.ProductSpecification;
import com.tota.eccom.exceptions.product.ProductAlreadyExistsException;
import com.tota.eccom.exceptions.product.ProductNotFoundException;
import com.tota.eccom.exceptions.product.ProductPriceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductDomain implements IProductDomain {

    private final ProductRepository productRepository;


    @Override
    public Product createProduct(ProductDTO productDTO) {

        if (findProductBySKU(productDTO.getSku()) != null) {
            throw new ProductAlreadyExistsException("Product already exists with given plu: " + productDTO.getSku());
        }

        Product product = productDTO.toProduct();

        log.info("Creating product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with given id: " + id));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = getProductById(id);

        log.info("Deleting product by id: {}", id);

        product.setStatus(Status.DELETED);

        productRepository.save(product);
    }

    @Override
    public Product updateProductById(Long id, ProductDTO productDTO) {
        Product product = getProductById(id);

        Product updatedProduct = productDTO.toUpdatedProduct(product);

        log.info("Updating product: {}", updatedProduct);

        return productRepository.save(updatedProduct);
    }

    @Override
    public Product patchProductById(Long id, ProductDTO productDTO) {

        Product product = getProductById(id);

        Product updatedProduct = productDTO.toPatchedProduct(product);

        log.info("Patching product: {}", updatedProduct);

        return productRepository.save(updatedProduct);
    }

    @Override
    public Product addProductPriceToProduct(Long id, ProductPriceDTO productPriceDTO) {

        Product product = getProductById(id);

        productPriceDTO.addProductPriceToProduct(product);

        log.info("Adding product price to product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public void deleteProductPriceFromProduct(Long id, Long priceId) {

        Product product = getProductById(id);

        log.info("Deleting product price id {} from product: {}", priceId, product);

        if (product.getProductPrices().stream().noneMatch(pp -> pp.getId().equals(priceId))) {
            throw new ProductPriceNotFoundException(String.format("Product price with id %s not found", priceId));
        }

        product.getProductPrices().removeIf(pp -> pp.getId().equals(priceId));

        productRepository.save(product);
    }

    @Override
    public Product addProductStockToProduct(Long id, ProductStockDTO productStockDTO) {

        Product product = getProductById(id);

        productStockDTO.addProductStockToProduct(product);

        log.info("Adding product stock to product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public Product getProductBySlug(String slug) {

        log.info("Getting product by slug: {}", slug);

        return productRepository.findBySlug(slug).orElseThrow(() -> new ProductNotFoundException("Product not found with given slug: " + slug));
    }

    @Override
    public Page<Product> searchProductsByTerm(String term, Pageable pageable) {

        log.info("Searching products by term: {}", term);

        Specification<Product> specification = ProductSpecification.searchProductsByTerm(term);

        return productRepository.findAll(specification, pageable);
    }

    private Product findProductBySKU(String sku) {
        return productRepository.findBySku(sku).orElse(null);
    }


}
