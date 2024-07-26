package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IProductService {

    Product createProduct(ProductDTO productDTO);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(Long id, ProductDTO productDTO);

    Product patchProductById(Long id, ProductDTO productDTO);

    Product addProductPriceToProduct(Long id, ProductPriceDTO productPriceDTO);

    void deleteProductPriceFromProduct(Long id, Long priceId);

    Product addProductStockToProduct(Long id, ProductStockDTO productStockDTO);

    Product getProductBySlug(String slug);

    Page<Product> searchProductsByTerm(String term, Pageable pageable);

    Product addProductCategoryToProduct(Long id, Long categoryId);

    Product addProductBrandToProduct(Long id, Long brandId);
}
