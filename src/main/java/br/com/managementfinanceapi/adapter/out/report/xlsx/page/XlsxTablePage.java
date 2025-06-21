package br.com.managementfinanceapi.adapter.out.report.xlsx.page;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.HeaderFooterWriter;
import br.com.managementfinanceapi.adapter.out.report.xlsx.writer.RowWriter;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;

@Component("tablePage")
public class XlsxTablePage implements XlsxPage<ReportTable> {

  private final RowWriter rowWriter;
  private final HeaderFooterWriter headerFooterWriter;

  public XlsxTablePage(
      RowWriter rowWriter,
      HeaderFooterWriter headerFooterWriter
  ) {
    this.rowWriter = rowWriter;
    this.headerFooterWriter = headerFooterWriter;
  }

  @Override
  public void createPage(Workbook workbook, ReportTable report) {
    Sheet sheet = workbook.createSheet(report.headerTitle());
    this.headerFooterWriter.writeHeader(sheet, report.getHeader().columns());
    for (Row<?> row : report.getRows()) {
      this.rowWriter.write(sheet, row);
    }
    Row<Object> footerColumns = report.getFooter().columns();
    this.headerFooterWriter.resizeHeaderAndFooter(sheet, footerColumns.columns());
    this.headerFooterWriter.writeFooter(sheet, footerColumns);
  }

}