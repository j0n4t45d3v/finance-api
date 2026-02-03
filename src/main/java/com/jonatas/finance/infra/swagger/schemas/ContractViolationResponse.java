package com.jonatas.finance.infra.swagger.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error contract violation")
public class ContractViolationResponse {

  public LocalDateTime timestamp;
  @Schema(example = "400")
  public int status;
  public ErrorDataContract data;

  public static class ErrorDataContract {
    @Schema(example = "contract_violation")
    public String type;
    public Validations error;
  }

  public static class Validations {
    @Schema(example = "name is required")
    public String name;
  }
}
