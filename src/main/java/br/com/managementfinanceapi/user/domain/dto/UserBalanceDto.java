package br.com.managementfinanceapi.user.domain.dto;


import java.math.BigDecimal;

public record UserBalanceDto(
    BigDecimal amount
) {
}
