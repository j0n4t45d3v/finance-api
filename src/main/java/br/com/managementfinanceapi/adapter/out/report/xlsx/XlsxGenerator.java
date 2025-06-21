package br.com.managementfinanceapi.adapter.out.report.xlsx;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import br.com.managementfinanceapi.adapter.out.report.xlsx.page.XlsxPage;
import br.com.managementfinanceapi.adapter.out.report.xlsx.page.XlsxTablePage;
import br.com.managementfinanceapi.application.core.domain.report.Report;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

@Component
@RequestScope
public class XlsxGenerator implements ReportGeneratorPort {

  @FunctionalInterface
  private interface CreatePage {
    void create(Workbook wb);
    
  }

  private final Map<String, XlsxPage<? extends Report>> page;
  private final List<CreatePage> pages;

  public XlsxGenerator(Map<String, XlsxPage<? extends Report>> page) {
    this.page = page;
    this.pages = new ArrayList<>();
  }

  @Override
  public byte[] generate() {
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      this.pages.forEach(page -> page.create(workbook));
      workbook.write(out);
      return out.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new byte[] {};
  }

  @Override
  public ReportGeneratorPort addPage(ReportTable page) {
    this.pages.add(workbook -> {
      XlsxTablePage pageCreator = (XlsxTablePage) this.page.get("tablePage");
      pageCreator.createPage(workbook, page);
    });
    return this;
  }

}