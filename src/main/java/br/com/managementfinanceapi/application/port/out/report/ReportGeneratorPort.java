package br.com.managementfinanceapi.application.port.out.report;

import br.com.managementfinanceapi.application.core.domain.report.ReportTable;

public interface ReportGeneratorPort {
  ReportGeneratorPort addPage(ReportTable report);
  byte[] generate();
}