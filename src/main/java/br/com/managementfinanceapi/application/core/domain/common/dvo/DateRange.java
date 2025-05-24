package br.com.managementfinanceapi.application.core.domain.common.dvo;

import java.time.LocalDate;

public record DateRange(
  LocalDate start,
  LocalDate end
) {
  public boolean rangeIsValid() {
    return this.start().isAfter(this.end());
  }
}