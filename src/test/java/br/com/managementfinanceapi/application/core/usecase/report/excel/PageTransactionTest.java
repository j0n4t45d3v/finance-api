package br.com.managementfinanceapi.application.core.usecase.report.excel;

import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Cell;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;
import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PageTransactionTest {

  @InjectMocks
  private PageTransaction pageTransactions;

  @Test
  @DisplayName("should generate correct income row in transaction page")
  void shouldGenerateCorrectIncomeRowInTransactionPage() {
    LocalDateTime currentTimestamp = LocalDateTime.now();
    TransactionDomain incomeTransactionMock = createIncomeTransaction(currentTimestamp);

    List<TransactionDomain> transactions = List.of(incomeTransactionMock);
    ReportTable reportStructsGenerated = this.pageTransactions.generate(transactions);

    assertThat(reportStructsGenerated.getRows())
        .satisfies(body -> {
          Row<Object> row = body.get(0);
          assertEquals(currentTimestamp, row.getCellValue(0));
          assertEquals("test page transactions", row.getCellValue(1));
          Cell<Object> cellTransactionType = row.get(2);
          assertEquals(TransactionType.INCOME.getDescription(), cellTransactionType.value());

          assertThat(cellTransactionType.getStyles())
              .contains(CellStyle.PAINT_INCOME)
              .contains(CellStyle.H_ALIGN_CENTER)
              .doesNotContain(CellStyle.PAINT_EXPENCE);

          assertEquals(BigDecimal.TEN, row.getCellValue(3));
          assertEquals(BigDecimal.ZERO, row.getCellValue(4));
        });
  }

  @Test
  @DisplayName("should generate correct expense row in transaction page")
  void shouldGenerateCorrectExpenseRowInTransactionPage() {
    LocalDateTime currentTimestamp = LocalDateTime.now();
    TransactionDomain expenseTransactionMock = createExpenseTransaction(currentTimestamp);

    List<TransactionDomain> transactions = List.of(expenseTransactionMock);
    ReportTable reportStructsGenerated = this.pageTransactions.generate(transactions);

    assertThat(reportStructsGenerated.getRows())
        .satisfies(body -> {
          Row<Object> row = body.get(0);
          assertEquals(currentTimestamp, row.getCellValue(0));
          assertEquals("expense test page transactions", row.getCellValue(1));
          Cell<Object> cellTransactionType = row.get(2);
          assertEquals(TransactionType.EXPENSE.getDescription(), cellTransactionType.value());

          assertThat(cellTransactionType.getStyles())
              .contains(CellStyle.PAINT_EXPENCE)
              .contains(CellStyle.H_ALIGN_CENTER)
              .doesNotContain(CellStyle.PAINT_INCOME);

          assertEquals(BigDecimal.ONE, row.getCellValue(3));
          assertEquals(BigDecimal.ZERO, row.getCellValue(4));
        });
  }

  @Test
  @DisplayName("should generate correct header and footer")
  void shouldGenerateCorrectHeaderAndFooter() {
    LocalDateTime currentTimestamp = LocalDateTime.now();
    TransactionDomain incomeTransactionMock = createIncomeTransaction(currentTimestamp);

    TransactionDomain expenseTransactionMock = createExpenseTransaction(currentTimestamp);

    List<TransactionDomain> transactions = List.of(incomeTransactionMock, expenseTransactionMock);
    ReportTable reportStructsGenerated = this.pageTransactions.generate(transactions);

    assertThat(reportStructsGenerated.getHeader())
        .satisfies(header -> {
          Row<String> headerColumns = header.columns();
          assertEquals("Transações", header.title());
          assertEquals("Data da Transação", headerColumns.getCellValue(0));
          assertEquals("Descrição", headerColumns.getCellValue(1));
          assertEquals("Tipo da Transação", headerColumns.getCellValue(2));
          assertEquals("Valor", headerColumns.getCellValue(3));
          assertEquals("Saldo", headerColumns.getCellValue(4));
        });

    assertThat(reportStructsGenerated.getFooter())
        .satisfies(footer -> {
          Row<Object> footerColumns = footer.columns();
          assertEquals("", footerColumns.getCellValue(0));
          assertEquals("", footerColumns.getCellValue(1));
          assertEquals("Total", footerColumns.getCellValue(2));
          assertEquals(BigDecimal.valueOf(9), footerColumns.getCellValue(3));
          assertEquals(BigDecimal.ZERO, footerColumns.getCellValue(4));
        });
  }

  private TransactionDomain createIncomeTransaction(LocalDateTime currentTimestamp) {
    return new TransactionDomain(
        1L,
        BigDecimal.TEN,
        TransactionType.INCOME,
        "test page transactions",
        currentTimestamp,
        null,
        null
    );
  }

  private TransactionDomain createExpenseTransaction(LocalDateTime currentTimestamp) {
    return new TransactionDomain(
        2L,
        BigDecimal.ONE,
        TransactionType.EXPENSE,
        "expense test page transactions",
        currentTimestamp,
        null,
        null
    );
  }
}