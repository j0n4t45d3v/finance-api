package br.com.managementfinanceapi.application.core.domain.report.dvo;

import java.math.BigDecimal;

import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;

public record Footer(
    Row<Object> columns
) {

  public Footer(int numColumns) {
    this(new Row<>(numColumns));
  }

  public void column(String value, CellStyle... styles) {
    this.columns.addColumn(Cell.of(value, styles));
  }

  public void columnMoney(BigDecimal value) {
    this.columns.addColumn(Cell.ofMoney(value));
  }

  public void columnEmpty() {
    this.columns.addColumn(Cell.of(""));
  }

}