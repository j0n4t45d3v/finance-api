package br.com.managementfinanceapi.application.core.usecase.report.excel;

import java.math.BigDecimal;
import java.util.List;

import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Cell;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Footer;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Header;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;
import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;

public class PageResumeByCategory implements CreateReportPagePort<List<CategoryTransactionSummary>> {

  private static final int NUM_COLUMNS = 4;

  @Override
  public ReportTable generate(List<CategoryTransactionSummary> categoryTransactionSummaries) {
    return new ReportTable(
      this.createHeader(),
      this.createBodyRows(categoryTransactionSummaries),
      this.createFooter(categoryTransactionSummaries)
    );
  }

  private Header createHeader() {
    Header header = new Header("Resumo por categoria", NUM_COLUMNS);
    header.column("Categoria", CellStyle.H_ALIGN_CENTER);
    header.column("Receita (R$)", CellStyle.H_ALIGN_END);
    header.column("Receita (%)", CellStyle.H_ALIGN_END);
    header.column("Despesa (R$)", CellStyle.H_ALIGN_END);
    header.column("Despesa (%)", CellStyle.H_ALIGN_END);
    return header;
  }

  private Footer createFooter(List<CategoryTransactionSummary> categoryTransactionSummaries) {
    Footer footer = new Footer(NUM_COLUMNS);
    CategoryTransactionSummary categoryTransactionSummary = categoryTransactionSummaries.get(0);
    footer.column("Total", CellStyle.H_ALIGN_END);
    footer.columnMoney(categoryTransactionSummary.totalIncome());
    footer.columnPercentege(BigDecimal.ONE);
    footer.columnMoney(categoryTransactionSummary.totalExpence()); 
    footer.columnPercentege(BigDecimal.ONE);
    return footer;
  }

  private List<Row<Object>> createBodyRows(List<CategoryTransactionSummary> categoryTransactionSummaries) {
    return categoryTransactionSummaries.stream()
        .map(resume -> {
          Row<Object> row = new Row<>(NUM_COLUMNS);
          row.addColumn(Cell.of(resume.name(), CellStyle.H_ALIGN_START));
          row.addColumn(Cell.ofMoney(resume.income()));
          row.addColumn(Cell.ofPercentege(resume.incomePercentage()));
          row.addColumn(resume.overflowCategoryCreditLimite() ? Cell.ofMoney(resume.expence()) : Cell.ofMoneyWarn(resume.expence()));
          row.addColumn(resume.overflowCategoryCreditLimite() ? Cell.ofPercentege(resume.expencePercentage()) : Cell.ofPercentegeWarn(resume.expencePercentage()));
          return row;
        }).toList();
  }

//   private Cell<Object> getExpenceCell(BigDecimal value, Function<BigDecimal, Cell<Object>> typeCell) {
// resume.overflowCategoryCreditLimite() ? Cell.ofMoney(resume.expence()) : Cell.ofMoneyWarn(resume.expence())
//   }

}