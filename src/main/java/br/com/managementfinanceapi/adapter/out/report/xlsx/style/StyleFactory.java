package br.com.managementfinanceapi.adapter.out.report.xlsx.style;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class StyleFactory {

  public StyleBuilder createHeaderOrFooter(Workbook workbook) {
    Font font = StyleFontBuilder
        .builder(workbook)
        .setColor(IndexedColors.WHITE)
        .setBold()
        .build();

    return StyleBuilder
        .builder(workbook)
        .setCellColor(Color.HEADER)
        .setFont(font);
  }

}