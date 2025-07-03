package br.com.managementfinanceapi.application.core.domain.report;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.report.dvo.Footer;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Header;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;

public class ReportTable extends Report {

  private Header header;
  private List<Row<Object>> rows;
  private Footer footer;

  public ReportTable(Header header, List<Row<Object>> rows, Footer footer) {
    this.header = header;
    this.rows = rows;
    this.footer = footer;
  }

  public String headerTitle() {
    return this.header.title();
  }

  public Header getHeader() {
    return header;
  }

  public List<Row<Object>> getRows() {
    return rows;
  }

  public Footer getFooter() {
    return footer;
  }


}