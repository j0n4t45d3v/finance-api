package br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell;

import org.apache.poi.ss.usermodel.Cell;

public interface CellWriter {
  boolean supports(Object value);
  void setValue(Cell cell, Object value);
}