package br.com.managementfinanceapi.infra.bean.report;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.usecase.report.excel.FinanceReportUseCase;
import br.com.managementfinanceapi.application.core.usecase.report.excel.PageResumeByCategory;
import br.com.managementfinanceapi.application.core.usecase.report.excel.PageTransaction;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.report.FinanceReportPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

@Component
public class ReportBean {

  @Bean
  public CreateReportPagePort<List<TransactionDomain>> createPageTransactions() {
    return new PageTransaction();
  }
  @Bean
  public CreateReportPagePort<List<CategoryTransactionSummary>> createPageResumeTransactionByCategory() {
    return new PageResumeByCategory();
  }

  @Bean
  public FinanceReportPort financeReport(
    ReportGeneratorPort reportGeneratorPort,
    SearchTransactionPort searchTransactionPort,
    SearchCategoryPort searchCategoryPort,
    CreateReportPagePort<List<TransactionDomain>> pageTrasactions,
    CreateReportPagePort<List<CategoryTransactionSummary>> pageResumeByCategory
  ) {
    return new FinanceReportUseCase(
      reportGeneratorPort,
      searchTransactionPort,
      searchCategoryPort,
      pageTrasactions,
      pageResumeByCategory
    );
  }
}