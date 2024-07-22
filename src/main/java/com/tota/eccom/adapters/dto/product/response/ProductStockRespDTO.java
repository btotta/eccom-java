package com.tota.eccom.adapters.dto.product.response;

import com.tota.eccom.domain.product.model.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductStockRespDTO {


    private Long id;
    private Integer available;


    public ProductStockRespDTO(ProductStock productStock) {
        this.id = productStock.getId();
        this.available = productStock.getQuantity() - productStock.getReservedQuantity();
    }
}
