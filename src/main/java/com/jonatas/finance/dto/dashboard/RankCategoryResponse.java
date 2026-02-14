package com.jonatas.finance.dto.dashboard;

import java.math.BigDecimal;

public record RankCategoryResponse(
    String category,
    String type,
    BigDecimal amount
) {
}
