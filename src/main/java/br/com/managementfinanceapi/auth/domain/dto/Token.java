package br.com.managementfinanceapi.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(
    String token,
    @JsonProperty("expired_at")
    Long expiresAt
) {}
