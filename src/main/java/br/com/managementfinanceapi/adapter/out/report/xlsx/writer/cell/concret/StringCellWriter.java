package br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.concret;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.CellWriter;

@Component
public class StringCellWriter implements CellWriter {

  @Override
  public boolean supports(Object value) {
    return value instanceof String;
  }

  @Override
  public void setValue(Cell cell, Object value) {
    cell.setCellValue((String) value);
  }

}