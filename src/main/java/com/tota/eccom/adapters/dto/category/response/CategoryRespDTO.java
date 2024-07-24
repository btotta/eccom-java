package com.tota.eccom.adapters.dto.category.response;


import com.tota.eccom.domain.product.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRespDTO {

    private Long id;
    private String name;
    private String description;
    private String slug;
    private Date createdAt;
    private Date updatedAt;


    public CategoryRespDTO(ProductCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.slug = category.getSlug();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }

}