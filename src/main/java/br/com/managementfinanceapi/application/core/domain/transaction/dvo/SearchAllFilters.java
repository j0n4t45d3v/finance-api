package br.com.managementfinanceapi.application.core.domain.transaction.dvo;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageFilter;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

public record SearchAllFilters(
  Long userId,
  DateRange dateRange,
  TransactionType typeTransaction,
  PageFilter page
) {

  public boolean isDateRangeValid() {
    return this.dateRange().rangeIsValid();
  }

  public boolean isUserIdMissing() {
    return this.userId() == null;
  }

}