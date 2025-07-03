package br.com.managementfinanceapi.adapter.out.report.xlsx.writer;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.style.StyleBuilder;
import br.com.managementfinanceapi.adapter.out.report.xlsx.style.StyleFactory;
import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.CellWriterContext;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Cell;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;

@Component
public class HeaderFooterWriter {

  private final CellWriterContext cellWriter;
  private final StyleFactory styleFactory;

  public HeaderFooterWriter(
      CellWriterContext cellWriter,
      StyleFactory styleFactory
  ) {
    this.cellWriter = cellWriter;
    this.styleFactory = styleFactory;
  }

  public void writeHeader(Sheet sheet, Row<?> columns) {
    this.fillHeaderFooter(sheet, columns, sheet.getFirstRowNum()+1);
  }

  public void writeFooter(Sheet sheet, Row<?> columns) {
    this.fillHeaderFooter(sheet, columns, sheet.getLastRowNum()+1);
  }

  public void resizeHeaderAndFooter(Sheet sheet, int numColumns) {
    for (int column = 0; column < numColumns; column++) {
      sheet.autoSizeColumn(column);
    }
  }

  private void fillHeaderFooter(Sheet sheet, Row<?> columns, int index) {
    org.apache.poi.ss.usermodel.Row row = sheet.createRow(index);
    org.apache.poi.ss.usermodel.Cell column;
    Cell<?> cell;
    StyleBuilder style;
    for (int columnIndex = 0; columnIndex < columns.columns(); columnIndex++) {
      style = this.styleFactory.createHeaderOrFooter(sheet.getWorkbook());
      column = row.createCell(columnIndex);
      cell = columns.get(columnIndex);
      this.cellWriter.write(column, cell.value());
      style.setStyles(cell.getStyles());
      column.setCellStyle(style.build());
    }
  }
}