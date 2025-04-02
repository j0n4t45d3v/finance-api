package br.com.managementfinanceapi.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token")
    Token accessToken,
    @JsonProperty("refresh_token")
    Token refreshToken
) {}
