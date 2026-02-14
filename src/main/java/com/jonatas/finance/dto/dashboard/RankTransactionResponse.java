package com.jonatas.finance.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RankTransactionResponse(
    String type,
    String category,
    BigDecimal amount,
    LocalDateTime transactionAt
) {
}
