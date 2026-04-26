package com.jonatas.finance.infra.swagger.schemas;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response")
public class SuccessLoginResponse {

  @Schema(example = "2026-04-22T12:58:45.351604855")
  public LocalDateTime timestamp;
  @Schema(example = "200")
  public int status;
  public PairToken data;

  public static class PairToken {
    public TokenResponse access;
    public TokenResponse refresh;
  }

  public static class TokenResponse {
    @Schema(example = "eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiJiYzYzMWUwZi1lMGQwL...")
    public String token;
    @Schema(example = "1777250748")
    public Long expiredAt;
  }
}
