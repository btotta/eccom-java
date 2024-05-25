package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IProductDomain {
    Product createProduct(ProductCreate productCreateDTO);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(Long id, ProductUpdate productUpdateDTO);


    Product addPriceToProduct(Long id, ProductCreatePrice productCreatePriceDTO);

    Page<Product> getAllProductsPaginated(Pageable pageable, String name, String description, Double price, String brand, String category);
}
