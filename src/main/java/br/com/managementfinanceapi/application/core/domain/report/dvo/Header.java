package br.com.managementfinanceapi.application.core.domain.report.dvo;

import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;

public record Header(
    String title,
    Row<String> columns
) {

  public Header(String title, int numColumns) {
    this(title, new Row<>(numColumns));
  }

  public void column(String column, CellStyle ...styles) {
    this.columns.addColumn(new Cell<String>(column, styles));
  }

  public int size() {
    return this.columns.size();
  }

}