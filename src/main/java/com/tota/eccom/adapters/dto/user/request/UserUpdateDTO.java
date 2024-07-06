package com.tota.eccom.adapters.dto.user.request;

import com.tota.eccom.domain.common.utils.EmailValidationUtil;
import com.tota.eccom.domain.user.model.User;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class UserUpdateDTO {

    private String name;
    private String email;

    public void validate() {

        if (email != null && !email.isEmpty() && !EmailValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

    }

    public void toUpdateUser(User user) {

        validate();

        if (name != null && !name.isBlank()) {
            user.setName(StringUtils.trimToEmpty(StringUtils.capitalize(name)));
        }

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

    }


}
