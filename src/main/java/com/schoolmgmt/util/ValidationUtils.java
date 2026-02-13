package com.schoolmgmt.util;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\-\\s]{6,20}$");

    private ValidationUtils() {
    }

    public static String requireValidEmail(String email) {
        Objects.requireNonNull(email, "email");
        String normalized = email.trim();
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return normalized.toLowerCase(Locale.ROOT);
    }

    public static String requireValidPhone(String phone) {
        Objects.requireNonNull(phone, "phone");
        String normalized = phone.trim();
        if (!PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        return normalized;
    }
}
