package com.jonatas.finance.dto.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public record DashboardFiltersRequest(
    LocalDate startDate,
    LocalDate endDate,
    Long accountId
) {

    public LocalDateTime getStartTimestamp() {
        return Optional.ofNullable(this.startDate())
            .orElse(LocalDate.now())
            .atStartOfDay();
    }

    public LocalDateTime getEndTimestamp() {
        return Optional.ofNullable(this.endDate())
            .orElse(LocalDate.now())
            .atTime(23, 59);
    }
}
