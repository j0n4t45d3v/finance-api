package com.jonatas.finance.analytic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RankTransactionResponse(
    String type, String category, BigDecimal amount, LocalDateTime transactionAt) {}
