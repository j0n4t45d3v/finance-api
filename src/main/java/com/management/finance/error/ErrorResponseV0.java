package com.management.finance.error;

import java.time.LocalDateTime;

public record ErrorResponseV0(
        int status,
        String error,
        LocalDateTime timestamp
) {
}
