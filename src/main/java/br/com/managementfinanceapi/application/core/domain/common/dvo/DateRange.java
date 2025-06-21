package br.com.managementfinanceapi.application.core.domain.common.dvo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record DateRange(
  LocalDate start,
  LocalDate end
) {

  public boolean rangeIsValid() {
    return this.start().isBefore(this.end());
  }

  public LocalDateTime startWithTime() {
    return this.start().atStartOfDay();
  }

  public LocalDateTime endWithTime() {
    return this.end().atTime(LocalTime.of(23, 59));
  }

}