package com.jonatas.finance.analytic;

import java.math.BigDecimal;

public record RankCategoryResponse(
    String category,
    String type,
    BigDecimal amount
) {
}
