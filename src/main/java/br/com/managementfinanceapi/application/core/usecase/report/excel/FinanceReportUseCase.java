package br.com.managementfinanceapi.application.core.usecase.report.excel;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.report.FinanceReportPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;

public class FinanceReportUseCase implements FinanceReportPort {

  private final ReportGeneratorPort reportGeneratorPort;
  private final SearchTransactionPort searchTransactionPort;
  private final SearchCategoryPort searchCategoryPort;
  private final CreateReportPagePort<List<TransactionDomain>> pageTrasactions;
  private final CreateReportPagePort<List<CategoryTransactionSummary>> pageResumeByCategory;

  public FinanceReportUseCase(
    ReportGeneratorPort reportGeneratorPort,
    SearchTransactionPort searchTransactionPort,
    SearchCategoryPort searchCategoryPort,
    CreateReportPagePort<List<TransactionDomain>> pageTrasactions,
    CreateReportPagePort<List<CategoryTransactionSummary>> pageResumeByCategory
  ) {
    this.reportGeneratorPort = reportGeneratorPort;
    this.searchTransactionPort = searchTransactionPort;
    this.searchCategoryPort = searchCategoryPort;
    this.pageTrasactions = pageTrasactions;
    this.pageResumeByCategory = pageResumeByCategory;
  }

  @Override
  public byte[] generate(Long userId, DateRange dateRange) {
    return this.reportGeneratorPort
        .addPage(this.createPageTrasactions(userId, dateRange))
        .addPage(this.createPageCategorySummaryTotals(userId, dateRange))
        .generate();
  }

  private ReportTable createPageTrasactions(Long userId, DateRange dateRange) {
    List<TransactionDomain> transactions = this.searchTransactionPort.allByUser(userId, dateRange);
    return this.pageTrasactions.generate(transactions);
  }

  private ReportTable createPageCategorySummaryTotals(Long userId, DateRange dateRange) {
    List<CategoryTransactionSummary> categoryTransactionSummaries = 
    this.searchCategoryPort.getSummaryIncomeAndExpencesTotals(userId, dateRange);
    return this.pageResumeByCategory.generate(categoryTransactionSummaries);
  }
}