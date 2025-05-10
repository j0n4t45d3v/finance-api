package br.com.managementfinanceapi.application.core.domain.user.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(
    String token,
    @JsonProperty("expired_at")
    Long expiresAt
) {}
