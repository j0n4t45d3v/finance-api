package com.jonatas.finance.adapter.security;

import java.time.Instant;

public record DecodedToken(String id,
                           String subject,
                           String type,
                           Instant expiredAt,
                           Instant issuedAt) {}
