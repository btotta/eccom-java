package com.tota.eccom.domain.common.utils;

import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class PasswordUtil {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/`~])[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/`~]{16,}$");

    private PasswordUtil() {
    }

    /**
     * Validates the new password and confirm new password.
     * Throws IllegalArgumentException if any validation fails.
     *
     * @param newPassword        the new password
     * @param confirmNewPassword the confirmation of the new password
     * @throws IllegalArgumentException if the passwords do not match, are too short, or do not meet the complexity requirements
     */
    public static void validatePasswordsEx(@NotEmpty String newPassword, @NotEmpty String confirmNewPassword) {

        if (isEmpty(newPassword)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (isEquals(newPassword, confirmNewPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (isLengthValid(newPassword)) {
            throw new IllegalArgumentException("Password must be at least 16 characters long");
        }

        if (!isPatternValid(newPassword)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }
    }

    /**
     * Validates the new password and confirm new password.
     * Returns false if any validation fails.
     *
     * @param newPassword        the new password
     * @param confirmNewPassword the confirmation of the new password
     * @return true if the passwords match, are long enough, and meet the complexity requirements; false otherwise
     */
    public static boolean validatePasswordsBol(@NotEmpty String newPassword, @NotEmpty String confirmNewPassword) {

        if (isEmpty(newPassword)) {
            return false;
        }

        if (isEquals(newPassword, confirmNewPassword)) {
            return false;
        }

        if (isLengthValid(newPassword)) {
            return false;
        }

        return isPatternValid(newPassword);
    }

    private static boolean isEquals(String newPassword, String confirmNewPassword) {
        return !newPassword.equals(confirmNewPassword);
    }

    private static boolean isLengthValid(String newPassword) {
        return newPassword.length() < 16;
    }

    private static boolean isPatternValid(String newPassword) {
        return PASSWORD_PATTERN.matcher(newPassword).matches();
    }

    private static boolean isEmpty(String newPassword) {
        return StringUtils.trimToEmpty(newPassword).isEmpty();
    }

    /**
     * Hashes the given password using BCrypt.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Validates the given password against the given hashed password.
     *
     * @param password       the password to validate
     * @param hashedPassword the hashed password to validate against
     * @return true if the password is valid; false otherwise
     */
    public static boolean validatePassword(String password, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, hashedPassword);
    }


}
