package br.com.managementfinanceapi.application.core.domain.user.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token")
    Token accessToken,
    @JsonProperty("refresh_token")
    Token refreshToken
) {}
