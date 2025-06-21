package br.com.managementfinanceapi.adapter.out.report.xlsx.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.style.StyleBuilder;
import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.CellWriterContext;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;

@Component
public class RowWriter {

  private final CellWriterContext cellWriter;

  public RowWriter(CellWriterContext cellWriter) {
    this.cellWriter = cellWriter;
  }

  public void write(Sheet sheet, Row<?> row) {
    Workbook workbook = sheet.getWorkbook();
    org.apache.poi.ss.usermodel.Row line = sheet.createRow(sheet.getLastRowNum() + 1);
    org.apache.poi.ss.usermodel.Cell cell;
    Object cellValue;
    StyleBuilder style;
    for (int column = 0; column < row.columns(); column++) {
      cell = line.createCell(column);
      cellValue = row.getCellValue(column);
      style = StyleBuilder
                  .builder(workbook)
                  .setStyles(row.get(column).getStyles());
      this.cellWriter.write(cell, cellValue);
      this.alternateRow(style, line.getRowNum());
      cell.setCellStyle(style.build());
    }
  }

  private void alternateRow(StyleBuilder style, int rowNumber) {
    if ((rowNumber % 2) == 0) {
      style.setAlternateRow();
    }
  }
}