package br.com.managementfinanceapi.infra.bean.report;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.usecase.report.excel.FinanceReportUseCase;
import br.com.managementfinanceapi.application.core.usecase.report.excel.PageTransaction;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.report.FinanceReportPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

@Component
public class ReportBean {

  @Bean
  public CreateReportPagePort createPage(SearchTransactionPort searchTransactionPort) {
  return new PageTransaction(searchTransactionPort);
  public CreateReportPagePort<List<TransactionDomain>> createPageTransactions() {
    return new PageTransaction();
  }
  @Bean

  @Bean
  public FinanceReportPort financeReport(ReportGeneratorPort reportGeneratorPort, CreateReportPagePort createReportPagePort) {
  return new FinanceReportUseCase(reportGeneratorPort, createReportPagePort);
  }
}