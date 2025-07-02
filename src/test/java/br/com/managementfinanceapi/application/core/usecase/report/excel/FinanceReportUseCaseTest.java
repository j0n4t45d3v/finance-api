package br.com.managementfinanceapi.application.core.usecase.report.excel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import br.com.managementfinanceapi.application.port.out.report.ReportGeneratorPort;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
class FinanceReportUseCaseTest {

  @Mock
  private ReportGeneratorPort reportGeneratorPort;
  @Mock
  private SearchTransactionPort searchTransactionPort;
  @Mock
  private SearchCategoryPort searchCategoryPort;
  @Mock
  private CreateReportPagePort<List<TransactionDomain>> pageTransactions;
  @Mock
  private CreateReportPagePort<List<CategoryTransactionSummary>> pageResumeByCategory;
  @InjectMocks
  private FinanceReportUseCase financeReportUseCase;

  @Test
  @DisplayName("should generate finance report")
  @MockitoSettings(strictness = Strictness.LENIENT)
  void shouldGenerateFinanceReport() {
    byte[] expectedReport = new byte[]{1, 2, 3};

    Mockito.when(this.searchTransactionPort.allByUser(anyLong(), any(DateRange.class)))
        .thenReturn(List.of(mock(TransactionDomain.class)));
    Mockito.when(this.searchCategoryPort.getSummaryIncomeAndExpencesTotals(anyLong(), any(DateRange.class)))
        .thenReturn(List.of(mock(CategoryTransactionSummary.class)));
    Mockito.when(this.pageTransactions.generate(anyList()))
        .thenReturn(new ReportTable(null, null, null));
    Mockito.when(this.pageResumeByCategory.generate(anyList()))
        .thenReturn(new ReportTable(null, null, null));
    Mockito.when(this.reportGeneratorPort.addPage(any(ReportTable.class)))
        .thenReturn(this.reportGeneratorPort);
    Mockito.when(this.reportGeneratorPort.generate())
        .thenReturn(expectedReport);

    byte[] reportGenerated =
        this.financeReportUseCase.generate(1L, new DateRange(LocalDate.now(), LocalDate.now()));

    assertEquals(expectedReport, reportGenerated);
  }

}
