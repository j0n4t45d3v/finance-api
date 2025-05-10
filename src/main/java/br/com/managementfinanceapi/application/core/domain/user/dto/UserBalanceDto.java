package br.com.managementfinanceapi.application.core.domain.user.dto;


import java.math.BigDecimal;

public record UserBalanceDto(
    BigDecimal amount
) {
}
