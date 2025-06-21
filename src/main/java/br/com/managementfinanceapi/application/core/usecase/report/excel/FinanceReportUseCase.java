package br.com.managementfinanceapi.application.core.usecase.report.excel;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.report.FinanceReportPort;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

public class FinanceReportUseCase implements FinanceReportPort {

  public final ReportGeneratorPort reportGeneratorPort;
  public final CreateReportPagePort createReportPagePort;

  public FinanceReportUseCase(
      ReportGeneratorPort reportGeneratorPort,
      CreateReportPagePort createReportPagePort
  ) {
    this.reportGeneratorPort = reportGeneratorPort;
    this.createReportPagePort = createReportPagePort;
  }

  @Override
  public byte[] generate(Long userId, DateRange dateRange) {
    return this.reportGeneratorPort
        .addPage(this.createReportPagePort.generate(userId, dateRange))
        .generate();
  }

}