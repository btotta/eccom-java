package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.product.request.ProductDTO;
import com.tota.eccom.adapters.dto.product.request.ProductPriceDTO;
import com.tota.eccom.adapters.dto.product.request.ProductStockDTO;
import com.tota.eccom.domain.product.model.Product;


public interface IProductDomain {

    Product createProduct(ProductDTO productDTO);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(Long id, ProductDTO productDTO);

    Product patchProductById(Long id, ProductDTO productDTO);

    Product addProductPriceToProduct(Long id, ProductPriceDTO productPriceDTO);

    void deleteProductPriceFromProduct(Long id, Long priceId);

    Product addProductStockToProduct(Long id, ProductStockDTO productStockDTO);
}
