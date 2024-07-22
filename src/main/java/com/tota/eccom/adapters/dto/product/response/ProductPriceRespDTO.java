package com.tota.eccom.adapters.dto.product.response;

import com.tota.eccom.domain.product.model.ProductPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductPriceRespDTO {

    private Long id;
    private Double price;
    private Integer quantity;
    private Date createdAt;
    private Date updatedAt;


    public ProductPriceRespDTO(ProductPrice pp){

        this.id = pp.getId();
        this.price = pp.getPrice().doubleValue();
        this.quantity = pp.getQuantity();
        this.createdAt = pp.getCreatedAt();
        this.updatedAt = pp.getUpdatedAt();
    }


}
