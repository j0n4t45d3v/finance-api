package br.com.managementfinanceapi.adapter.out.report.xlsx.writer.cell;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

@Component
public class CellWriterContext {

  private final List<CellWriter> cellWriterStrategies;

  public CellWriterContext(List<CellWriter> cellWriterStrategies) {
    this.cellWriterStrategies = cellWriterStrategies;
  }

  public void write(Cell cell, Object value) {
    for (CellWriter cellWriter : this.cellWriterStrategies) {
      if (cellWriter.supports(value)) {
        cellWriter.setValue(cell, value);
      }
    }
  }

}