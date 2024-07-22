package com.tota.eccom.domain.product;

import com.tota.eccom.adapters.dto.product.ProductCreate;
import com.tota.eccom.adapters.dto.product.ProductCreatePrice;
import com.tota.eccom.adapters.dto.product.ProductCreateProductPackage;
import com.tota.eccom.adapters.dto.product.ProductUpdate;
import com.tota.eccom.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface IProductDomain {

    Product createProduct(ProductCreate productCreateDTO);

    Product getProductByPLU(String plu);

    void deleteProductById(Long id);

    Product getProductById(Long id);

    Product addProductPackageToProduct(Long id, ProductCreateProductPackage productCreateProductPackage);

    void deleteProductPackageFromProduct(Long id, Long packageId);
}
