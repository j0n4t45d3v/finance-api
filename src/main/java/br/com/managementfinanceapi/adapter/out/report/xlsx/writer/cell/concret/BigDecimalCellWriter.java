package br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.concret;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.CellWriter;

@Component
public class BigDecimalCellWriter implements CellWriter {

  @Override
  public boolean supports(Object value) {
    return value instanceof BigDecimal;
  }

  @Override
  public void setValue(Cell cell,Object value) {
    cell.setCellValue(((BigDecimal) value).doubleValue());
  }
  
}