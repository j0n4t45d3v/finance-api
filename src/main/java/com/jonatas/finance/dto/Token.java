package com.jonatas.finance.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(@JsonProperty("token") String value, LocalDateTime expiredAt) {
}
