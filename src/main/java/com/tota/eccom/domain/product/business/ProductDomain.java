package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.domain.category.model.Category;
import com.tota.eccom.domain.brand.repository.BrandRepository;
import com.tota.eccom.domain.category.repository.CategoryRepository;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.domain.product.repository.spec.ProductSpecification;
import com.tota.eccom.exceptions.generic.ResourceAlreadyExistsException;
import com.tota.eccom.exceptions.generic.ResourceNotFoundException;
import com.tota.eccom.util.SlugUtil;
import com.tota.eccom.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductDomain implements IProductDomain {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;


    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {

        if (productDTO.getName() != null && findProductBySlug(SlugUtil.makeSlug(productDTO.getName())) != null) {
            throw new ResourceAlreadyExistsException("Product with given slug already exists");
        }

        if (productDTO.getSku() != null && findProductBySKU(productDTO.getSku()) != null) {
            throw new ResourceAlreadyExistsException("Product already exists with given plu: " + productDTO.getSku());
        }

        Product product = productDTO.toProduct();

        log.info("Creating product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id: " + id));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = getProductById(id);

        log.info("Deleting product by id: {}", id);

        product.setStatus(Status.DELETED);

        productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProductById(Long id, ProductDTO productDTO) {

        if (productDTO.getName() != null) {
            validateProductSlug(SlugUtil.makeSlug(productDTO.getName()), id);
        }

        Product product = getProductById(id);

        Product updatedProduct = productDTO.toUpdatedProduct(product);

        log.info("Updating product: {}", updatedProduct);

        return productRepository.save(updatedProduct);
    }

    @Override
    @Transactional
    public Product patchProductById(Long id, ProductDTO productDTO) {

        if (productDTO.getName() != null) {
            validateProductSlug(SlugUtil.makeSlug(productDTO.getName()), id);
        }

        Product product = getProductById(id);

        Product updatedProduct = productDTO.toPatchedProduct(product);

        log.info("Patching product: {}", updatedProduct);

        return productRepository.save(updatedProduct);
    }

    @Override
    @Transactional
    public Product addProductPriceToProduct(Long id, ProductPriceDTO productPriceDTO) {

        Product product = getProductById(id);

        productPriceDTO.addProductPriceToProduct(product);

        log.info("Adding product price to product: {}", product);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProductPriceFromProduct(Long id, Long priceId) {

        Product product = getProductById(id);

        log.info("Deleting product price id {} from product: {}", priceId, product);

        if (product.getProductPrices().stream().noneMatch(pp -> pp.getId().equals(priceId))) {
            throw new ResourceNotFoundException(String.format("Product price with id %s not found", priceId));
        }

        product.getProductPrices().removeIf(pp -> pp.getId().equals(priceId));

        productRepository.save(product);
    }

    @Override
    @Transactional
    public Product addProductStockToProduct(Long id, ProductStockDTO productStockDTO) {

        Product product = getProductById(id);

        productStockDTO.addProductStockToProduct(product);

        log.info("Adding product stock to product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public Product getProductBySlug(String slug) {

        log.info("Getting product by slug: {}", slug);

        return productRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Product not found with given slug: " + slug));
    }

    @Override
    public Page<Product> searchProductsByTerm(String term, Pageable pageable) {

        log.info("Searching products by term: {}", term);

        Specification<Product> specification = ProductSpecification.searchProductsByTerm(term);

        return productRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Product addProductCategoryToProduct(Long id, Long categoryId) {

        Category category = findCategoryById(categoryId);

        Product product = getProductById(id);

        if (product.getProductCategories() == null) {
            product.setProductCategories(new ArrayList<>());
        }

        if (product.getProductCategories().stream().anyMatch(pc -> pc.getId().equals(categoryId))) {
            throw new ResourceAlreadyExistsException("Product already has category with given id");
        }

        product.getProductCategories().add(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product addProductBrandToProduct(Long id, Long brandId) {

        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ResourceNotFoundException("Product brand not found with given id: " + brandId));

        Product product = getProductById(id);

        product.setBrand(brand);

        return productRepository.save(product);
    }

    private void validateProductSlug(String slug, Long id) {
        Product existingProduct = findProductBySlug(slug);
        if (existingProduct != null && !existingProduct.getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Product with given slug already exists");
        }
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product category not found with given id: " + id));
    }

    private Product findProductBySKU(String sku) {
        return productRepository.findBySku(sku).orElse(null);
    }

    private Product findProductBySlug(String slug) {
        return productRepository.findBySlug(slug).orElse(null);
    }


}
