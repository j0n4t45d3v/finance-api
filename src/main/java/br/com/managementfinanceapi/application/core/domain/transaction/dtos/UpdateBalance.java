package br.com.managementfinanceapi.application.core.domain.transaction.dtos;

import java.math.BigDecimal;

public record UpdateBalance(BigDecimal amount, short month, short year, Long userId) {
}
