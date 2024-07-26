package com.tota.eccom.adapters.dto.brand.response;


import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.brand.model.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandRespDTO {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private Status status;


    public BrandRespDTO(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.slug = brand.getSlug();
        this.description = brand.getDescription();
        this.status = brand.getStatus();

    }


}
