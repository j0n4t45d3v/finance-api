package br.com.managementfinanceapi.transaction.domain.dtos;

import java.math.BigDecimal;

public record UpdateBalance(BigDecimal amount, short month, short year, Long userId) {
}
