package com.tota.eccom.adapters.dto.brand.request;


import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.brand.model.Brand;
import com.tota.eccom.util.SlugUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {


    private String name;
    private String description;
    private Status status;


    public Brand toBrand() {

        validate();

        return Brand.builder()
                .name(name)
                .description(description)
                .status(Status.ACTIVE)
                .slug(SlugUtil.makeSlug(name))
                .build();
    }


    public void toUpdatedBrand(Brand brand) {

        validate();

        brand.setName(name);
        brand.setDescription(description);
        brand.setSlug(SlugUtil.makeSlug(name));

        if (status != null) {
            brand.setStatus(status);
        }
    }


    private void validate() {

        validateField(name, "Name is required");
        validateField(description, "Description is required");

    }

    private void validateField(String field, String message) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

}
