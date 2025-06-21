package br.com.managementfinanceapi.application.core.usecase.report.excel;

import java.math.BigDecimal;
import java.util.List;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Cell;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Footer;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Header;
import br.com.managementfinanceapi.application.core.domain.report.dvo.Row;
import br.com.managementfinanceapi.application.core.domain.report.enums.CellStyle;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.in.report.CreateReportPagePort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;

public class PageTransaction implements CreateReportPagePort {

  private final SearchTransactionPort searchTransactionPort;
  private static final int NUM_COLUMNS = 5;

  public PageTransaction(SearchTransactionPort searchTransactionPort) {
    this.searchTransactionPort = searchTransactionPort;
  }

  @Override
  public ReportTable generate(Long userId, DateRange dateRange) {
    List<TransactionDomain> transactions = this.searchTransactionPort.allByUser(userId, dateRange);
    Header header = this.createHeader();
    List<Row<Object>> body = this.createBodyRows(transactions);
    Footer footer = createFooter(this.getTotalTransaction(transactions));
    return new ReportTable(header, body, footer);
  }

  private Header createHeader() {
    Header header = new Header("Transações", NUM_COLUMNS);
    header.column("Data da Transação", CellStyle.H_ALIGN_CENTER);
    header.column("Descrição", CellStyle.H_ALIGN_START);
    header.column("Tipo da Transação", CellStyle.H_ALIGN_CENTER);
    header.column("Valor", CellStyle.H_ALIGN_END);
    header.column("Saldo", CellStyle.H_ALIGN_END);
    return header;
  }

  private Footer createFooter(BigDecimal totalTransactions) {
    Footer footer = new Footer(NUM_COLUMNS);
    footer.columnEmpty();
    footer.columnEmpty();
    footer.column("Total", CellStyle.H_ALIGN_END);
    footer.columnMoney(totalTransactions);
    footer.columnMoney(BigDecimal.ZERO);
    return footer;
  }

  private List<Row<Object>> createBodyRows(List<TransactionDomain> transactions) {
    return transactions.stream()
        .map(transaction -> {
          Row<Object> row = new Row<>(NUM_COLUMNS);
          row.addColumn(Cell.ofTimestamp(transaction.getDate()));
          row.addColumn(Cell.of(transaction.getDescription()));
          row.addColumn(Cell.of(transaction.getTypeTransactionDescription(), CellStyle.H_ALIGN_CENTER));
          row.addColumn(Cell.ofMoney(transaction.getAmount()));
          row.addColumn(Cell.ofMoney(BigDecimal.ZERO));
          return row;
        }).toList();
  }

  private BigDecimal getTotalTransaction(List<TransactionDomain> transactions) {
    return transactions.stream()
        .map(transaction -> transaction.getAmount().multiply(BigDecimal.valueOf(transaction.isExpence() ? -1 : 1)))
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

}