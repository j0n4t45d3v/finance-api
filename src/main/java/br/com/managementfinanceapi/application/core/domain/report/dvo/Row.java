package br.com.managementfinanceapi.application.core.domain.report.dvo;

import java.util.ArrayList;
import java.util.List;

public record Row<T>(
    List<Cell<T>> cells,
    int size
) {

   public Row(int size) {
    this(new ArrayList<>(size), 0);
  }

  public int getColumn(Cell<?> cell) {
    return cells.indexOf(cell);
  }

  public T getCellValue(int column){
    Cell<T> value = this.get(column);
    return value != null ? value.value() : null;
  }

  public Cell<T> get(int column){
    return this.cells.get(column);
  }

  public int columns() {
    return this.cells.size();
  }

  public void addColumn(Cell<T> cell) {
    this.cells.add(cell);
  }

}