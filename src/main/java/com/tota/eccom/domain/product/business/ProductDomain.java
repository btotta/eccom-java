package com.tota.eccom.domain.product.business;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreateProductPackage;
import com.tota.eccom.domain.enums.Status;
import com.tota.eccom.domain.product.IProductDomain;
import com.tota.eccom.domain.product.model.Product;
import com.tota.eccom.domain.product.model.ProductPackage;
import com.tota.eccom.domain.product.repository.ProductRepository;
import com.tota.eccom.exceptions.product.ProductAlreadyExistsException;
import com.tota.eccom.exceptions.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductDomain implements IProductDomain {

    private final ProductRepository productRepository;


    @Override
    public Product createProduct(ProductCreate productCreateDTO) {

        if (findProductByPLU(productCreateDTO.getPlu()) != null) {
            throw new ProductAlreadyExistsException("Product already exists with given plu: " + productCreateDTO.getPlu());
        }

        Product product = productCreateDTO.toProduct();

        log.info("Creating product: {}", product);

        return productRepository.save(product);
    }

    private Product findProductByPLU(String plu) {
        return productRepository.findByPlu(plu).orElse(null);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with given id: " + id));
    }

    @Override
    public Product addProductPackageToProduct(Long id, ProductCreateProductPackage productCreateProductPackage) {

        Product product = getProductById(id);

        ProductPackage productPackage = productCreateProductPackage.toProductPackage();

        if (product.getProductPackages().stream().anyMatch(pp -> pp.getId().equals(productPackage.getId()))) {
            throw new ProductAlreadyExistsException(String.format("Product package with type %s already exists with given id: %s", productPackage.getType(), productPackage.getId()));
        }

        product.getProductPackages().add(productPackage);

        log.info("Adding product package to product: {}", product);

        return productRepository.save(product);
    }

    @Override
    public void deleteProductPackageFromProduct(Long id, Long packageId) {

        Product product = getProductById(id);

        log.info("Deleting product package id {} from product: {}", packageId, product);

        if (product.getProductPackages().stream().noneMatch(pp -> pp.getId().equals(packageId))) {
            throw new ProductNotFoundException(String.format("Product package with id %s not found", packageId));
        }

        product.getProductPackages().removeIf(pp -> pp.getId().equals(packageId));

        productRepository.save(product);
    }

    @Override
    public Product getProductByPLU(String plu) {

        log.info("Getting product by plu: {}", plu);

        Product product = findProductByPLU(plu);

        if (product == null) {
            throw new ProductNotFoundException("Product not found with given plu: " + plu);
        }

        return product;
    }

    @Override
    public void deleteProductById(Long id) {

        Product product = getProductById(id);

        log.info("Deleting product by id: {}", id);

        product.setStatus(Status.DELETED);

        productRepository.save(product);
    }


}
