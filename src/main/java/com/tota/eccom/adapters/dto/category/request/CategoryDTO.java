package com.tota.eccom.adapters.dto.category.request;


import com.tota.eccom.util.enums.Status;
import com.tota.eccom.domain.product.model.ProductCategory;
import com.tota.eccom.util.SlugUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {


    private String name;
    private String description;
    private Status status;


    public ProductCategory toCategory() {

        validate();

        return ProductCategory.builder()
                .name(name)
                .description(description)
                .slug(SlugUtil.makeSlug(name))
                .status(Status.ACTIVE)
                .build();
    }


    public void toUpdatedCategory(ProductCategory category) {

        validate();

        category.setName(name);
        category.setDescription(description);
        category.setSlug(SlugUtil.makeSlug(name));

        if (status != null) {
            category.setStatus(status);
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
