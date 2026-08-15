package com.jonatas.finance.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Token(@JsonProperty("token") String value, Long expiredAt) {
}
