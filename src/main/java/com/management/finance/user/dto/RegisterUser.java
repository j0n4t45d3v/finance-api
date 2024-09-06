package com.management.finance.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterUser(
        String email,
        String password,
        @JsonProperty("confirm_password")
        String confirmPassword
) {

    public boolean confirmPasswordEqualsPassword() {
        return this.confirmPassword.equals(this.password);
    }
}
