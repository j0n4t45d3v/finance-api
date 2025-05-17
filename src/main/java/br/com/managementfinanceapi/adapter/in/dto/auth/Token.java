package br.com.managementfinanceapi.adapter.in.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(
    String token,
    @JsonProperty("expired_at")
    Long expiresAt
) {}