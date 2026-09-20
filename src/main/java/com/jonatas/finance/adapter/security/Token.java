package com.jonatas.finance.adapter.security;

import java.time.Instant;

public record Token(String value, Instant expiration) {}