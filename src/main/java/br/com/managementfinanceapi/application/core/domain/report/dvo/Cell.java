package br.com.managementfinanceapi.application.core.domain.report.dvo;

import static br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle.H_ALIGN_CENTER;
import static br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle.MONEY_FORMAT;
import static br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle.PERCENTEGE_FORMAT;
import static br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle.TIMESTAMP_FORMAT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;

public record Cell<T>(
    T value,
    CellStyle ...styles
) {

  public static Cell<Object> ofTimestamp(LocalDateTime timestamp) {
    return new Cell<>(timestamp, TIMESTAMP_FORMAT, H_ALIGN_CENTER);
  }

  public static Cell<Object> of(String value, CellStyle ...style) {
    return new Cell<>(value, style);
  }

  public static Cell<Object> ofMoney(BigDecimal value) {
    return new Cell<>(value, MONEY_FORMAT);
  }

  public static Cell<Object> ofPercentege(BigDecimal value) {
    return new Cell<>(value, PERCENTEGE_FORMAT);
  }

  public List<CellStyle> getStyles() {
    return List.of(styles);
  }

  @Override
  public final String toString() {
      return this.value.toString();
  }
}