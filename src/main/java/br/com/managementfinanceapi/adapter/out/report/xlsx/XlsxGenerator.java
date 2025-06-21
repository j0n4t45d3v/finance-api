package br.com.managementfinanceapi.adapter.out.report.xlsx;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.adapter.out.report.xlsx.page.XlsxPage;
import br.com.managementfinanceapi.adapter.out.report.xlsx.page.XlsxTablePage;
import br.com.managementfinanceapi.application.core.domain.report.Report;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

@Component
public class XlsxGenerator implements ReportGeneratorPort {

  private final Workbook workbook;
  private final ByteArrayOutputStream out;
  private final Map<String, XlsxPage<? extends Report>> page;

  public XlsxGenerator(Map<String, XlsxPage<? extends Report>> page) {
    this.out = new ByteArrayOutputStream();
    this.workbook = new XSSFWorkbook();
    this.page = page;
  }

  @Override
  public byte[] generate() {
    try {
      workbook.write(out);
      return out.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        workbook.close();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return new byte[] {};
  }

  @Override
  public ReportGeneratorPort addPage(ReportTable page) {
    XlsxTablePage pageCreator = (XlsxTablePage) this.page.get("tablePage");
    pageCreator.createPage(this.workbook, page);
    return this;
  }

}