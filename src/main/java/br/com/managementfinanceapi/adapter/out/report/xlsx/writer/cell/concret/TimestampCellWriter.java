package br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.concret;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell.CellWriter;

@Component
public class TimestampCellWriter implements CellWriter {

  private static final String TIMESTAMP_PATTERN = "dd/MM/yyyy HH:mm:ss";
  private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

  @Override
  public boolean supports(Object value) {
    return value instanceof LocalDateTime;
  }

  @Override
  public void setValue(Cell cell, Object value) {
    cell.setCellValue(((LocalDateTime) value).format(TIMESTAMP_FORMAT));
  }

}