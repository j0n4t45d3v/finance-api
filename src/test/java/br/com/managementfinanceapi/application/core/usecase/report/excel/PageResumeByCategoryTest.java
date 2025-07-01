package br.com.managementfinanceapi.application.core.usecase.report.excel;

import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;
import java.math.BigDecimal;
import java.util.List;

import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageResumeByCategoryTest {

  @InjectMocks
  private PageResumeByCategory pageResumeByCategory;

  @Test
  @DisplayName("should generate correct header and footer")
  void shouldGenerateCorrectHeaderAndFooter() {
    CategoryTransactionSummary categoryTransactionSummary = new CategoryTransactionSummary(
        "Test category",
        BigDecimal.valueOf(100),
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN
    );

    ReportTable pageSummaryIncomeXExpenseByCategory =
        this.pageResumeByCategory.generate(List.of(categoryTransactionSummary));

    assertThat(pageSummaryIncomeXExpenseByCategory.getHeader())
        .satisfies(header -> {
          Row<String> headerColumns = header.columns();
          assertEquals("Resumo por categoria", header.title());
          assertEquals("Categoria", headerColumns.getCellValue(0));
          assertEquals("Receita (R$)", headerColumns.getCellValue(1));
          assertEquals("Receita (%)", headerColumns.getCellValue(2));
          assertEquals("Despesa (R$)", headerColumns.getCellValue(3));
          assertEquals("Despesa (%)", headerColumns.getCellValue(4));
        });

    assertThat(pageSummaryIncomeXExpenseByCategory.getFooter())
        .satisfies(footer -> {
          Row<Object> footerColumns = footer.columns();
          assertEquals("Total", footerColumns.getCellValue(0));
          assertEquals(BigDecimal.TEN, footerColumns.getCellValue(1));
          assertEquals(BigDecimal.ONE, footerColumns.getCellValue(2));
          assertEquals(BigDecimal.TEN, footerColumns.getCellValue(3));
          assertEquals(BigDecimal.ONE, footerColumns.getCellValue(4));
        });
  }

  @Test
  @DisplayName("should generate correct body in summary transaction by category page")
  void shouldGenerateCorrectBodyInSummaryTransactionByCategoryPage() {
    CategoryTransactionSummary categoryTransactionSummary = new CategoryTransactionSummary(
        "Test category",
        BigDecimal.valueOf(100),
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN
    );

    ReportTable pageSummaryIncomeXExpenseByCategory =
        this.pageResumeByCategory.generate(List.of(categoryTransactionSummary));

    assertThat(pageSummaryIncomeXExpenseByCategory.getRows())
        .satisfies(body -> {
          Row<Object> row = body.get(0);
          assertEquals("Test category", row.getCellValue(0));
          assertEquals(BigDecimal.TEN, row.getCellValue(1));
          assertEquals(BigDecimal.ONE.setScale(4, HALF_UP), row.getCellValue(2));
          assertEquals(BigDecimal.TEN, row.getCellValue(3));
          assertEquals(BigDecimal.ONE.setScale(4, HALF_UP), row.getCellValue(4));
        });
  }

  @Test
  @DisplayName("should paint expense value and percentage like warn when expense value overflow credit limit in category")
  void shouldPaintExpenseValueAndPercentageLikeWarnWhenExpenseValueOverflowCreditLimitInCategory() {
    CategoryTransactionSummary categoryTransactionSummary = new CategoryTransactionSummary(
        "Test category 2",
        BigDecimal.ONE,
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN,
        BigDecimal.TEN
    );

    ReportTable pageSummaryIncomeXExpenseByCategory =
        this.pageResumeByCategory.generate(List.of(categoryTransactionSummary));

    assertThat(pageSummaryIncomeXExpenseByCategory.getRows())
        .satisfies(body -> {
          Row<Object> row = body.get(0);
          assertThat(row.get(3).getStyles())
              .contains(CellStyle.WARN);
          assertThat(row.get(4).getStyles())
              .contains(CellStyle.WARN);
        });
  }

}
