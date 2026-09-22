package com.jonatas.finance.adapter.security;

import java.time.Instant;

public record TokenInfo(String value, Instant expiration) {}