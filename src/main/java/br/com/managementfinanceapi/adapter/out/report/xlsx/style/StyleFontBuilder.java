package br.com.managementfinanceapi.adapter.out.report.xlsx.style;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class StyleFontBuilder {
  private final Font fontStyle;

  public StyleFontBuilder(Workbook workbook) {
    this.fontStyle = workbook.createFont();
  }

  public static StyleFontBuilder builder(Workbook workbook) {
    return new StyleFontBuilder(workbook);
  }

  public StyleFontBuilder setColor(Color color) {
    this.fontStyle.setColor(color.getValue().getIndex());
    return this;
  }

  public StyleFontBuilder setColor(IndexedColors color) {
    this.fontStyle.setColor(color.getIndex());
    return this;
  }

  public StyleFontBuilder setBold() {
    this.fontStyle.setBold(true);
    return this;
  }

  public Font build() {
    return this.fontStyle;
  }
}