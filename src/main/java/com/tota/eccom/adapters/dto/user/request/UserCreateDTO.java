package com.tota.eccom.adapters.dto.user.request;

import com.tota.eccom.util.enums.Status;
import com.tota.eccom.util.EmailValidationUtil;
import com.tota.eccom.util.PasswordUtil;
import com.tota.eccom.domain.user.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCreateDTO {

    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;
    @NotEmpty(message = "Confirm password is required")
    private String confirmPassword;

    public void validate() {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (!EmailValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        PasswordUtil.validatePasswordsEx(password, confirmPassword);
    }

    public User toUser() {
        validate();

        return User.builder()
                .name(name)
                .email(email)
                .password(PasswordUtil.hashPassword(password))
                .roles(new HashSet<>())
                .status(Status.ACTIVE)
                .build();
    }
}
