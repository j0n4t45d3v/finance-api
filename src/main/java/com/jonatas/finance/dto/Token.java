package com.jonatas.finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(@JsonProperty("token") String value, Long expiredAt) {
}
