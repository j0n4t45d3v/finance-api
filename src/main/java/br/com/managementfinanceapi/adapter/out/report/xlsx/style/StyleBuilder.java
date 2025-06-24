package br.com.managementfinanceapi.adapter.out.report.xlsx.style;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;

public class StyleBuilder {

  @FunctionalInterface
  public interface Style {
    void set(StyleBuilder style);
  }

  private final XSSFCellStyle style;
  private final CreationHelper createHelper;
  private static final Map<CellStyle, Style> MAP_STYLES = new EnumMap<>(CellStyle.class);

  static {
    MAP_STYLES.put(CellStyle.MONEY_FORMAT, StyleBuilder::setMoneyFormat);
    MAP_STYLES.put(CellStyle.PERCENTEGE_FORMAT, StyleBuilder::setPercentegeFormat);
    MAP_STYLES.put(CellStyle.WARN, (builder)  -> builder.setCellColor(IndexedColors.ORANGE));
    MAP_STYLES.put(CellStyle.PAINT_INCOME, (builder)  -> builder.setCellColor(IndexedColors.LIGHT_GREEN));
    MAP_STYLES.put(CellStyle.PAINT_EXPENCE, (builder)  -> builder.setCellColor(IndexedColors.LIGHT_ORANGE));
    MAP_STYLES.put(CellStyle.ALTERNETE_ROW, StyleBuilder::setAlternateRow);
    MAP_STYLES.put(CellStyle.H_ALIGN_END, StyleBuilder::setAlignRight);
    MAP_STYLES.put(CellStyle.H_ALIGN_START, StyleBuilder::setAlignLeft);
    MAP_STYLES.put(CellStyle.H_ALIGN_CENTER, StyleBuilder::setAlignCenter);
  }

  public StyleBuilder(XSSFCellStyle style, CreationHelper createHelper) {
    this.style = style;
    this.createHelper = createHelper;
  }

  public static StyleBuilder builder(Workbook workbook) {
    return new StyleBuilder((XSSFCellStyle) workbook.createCellStyle(), workbook.getCreationHelper());
  }

  public StyleBuilder setAlternateRow() {
    return this.setCellColor(Color.ZEBRA);
  }

  public StyleBuilder setCellColor(Color color) {
    this.style.setFillForegroundColor(color.getValue());
    this.style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return this;
  }

  public StyleBuilder setCellColor(IndexedColors color) {
    this.style.setFillForegroundColor(color.getIndex());
    this.style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return this;
  }

  public StyleBuilder setMoneyFormat() {
    this.style.setDataFormat(this.createHelper.createDataFormat().getFormat("\"R$\" #,##0.00"));
    return this;
  }

  public StyleBuilder setPercentegeFormat() {
    this.style.setDataFormat(this.createHelper.createDataFormat().getFormat("0.00%"));
    return this;
  }

  public StyleBuilder setFont(Font font) {
    this.style.setFont(font);
    return this;
  }

  public StyleBuilder setAlignCenter() {
    this.style.setAlignment(HorizontalAlignment.CENTER);
    return this;
  }
  public StyleBuilder setAlignRight() {
    this.style.setAlignment(HorizontalAlignment.RIGHT);
    return this;
  }
  public StyleBuilder setAlignLeft() {
    this.style.setAlignment(HorizontalAlignment.LEFT);
    return this;
  }

  public StyleBuilder setStyles(List<CellStyle> styles) {
    for (CellStyle cellStyle : styles) {
      Style styleSetter = MAP_STYLES.get(cellStyle);
      if(styleSetter != null) {
        styleSetter.set(this);
      }
    }
    return this;
  }

  public XSSFCellStyle build() {
    return this.style;
  }

}