package br.com.managementfinanceapi.transaction.domain.dtos;

import br.com.managementfinanceapi.transaction.domain.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
    Long id,
    String description,
    String type,
    BigDecimal amount,
    String category,
    LocalDateTime date,
    @JsonProperty("user_id")
    Long userId
) {
  public TransactionDto(Transaction transaction) {
    this(
        transaction.getId(),
        transaction.getDescription(),
        transaction.getType().name(),
        transaction.getAmount(),
        transaction.getCategory().getName(),
        transaction.getDate(),
        transaction.getUser().getId()
    );
  }
}
