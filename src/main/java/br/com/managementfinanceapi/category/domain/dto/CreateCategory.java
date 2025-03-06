package br.com.managementfinanceapi.category.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateCategory(
    @NotNull(message = "O campo 'user_id' é obrigatório")
    @JsonProperty("user_id")
    Long userId,
    @NotNull(message = "O campo 'name' é obrigatório")
    String name,
    @JsonProperty("credit_limit")
    BigDecimal creditLimit
) {
}
