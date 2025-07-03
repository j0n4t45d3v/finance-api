package br.com.managementfinanceapi.application.core.domain.transaction.dvo;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageFilter;
import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;
import br.com.managementfinanceapi.application.core.domain.common.exception.InvalidDateRangeException;
import br.com.managementfinanceapi.application.core.domain.transaction.enums.TransactionType;

public record SearchAllFilters(
  Long userId,
  DateRange dateRange,
  TransactionType typeTransaction,
  PageFilter page
) {

  public void validate() {
    if (this.isUserIdMissing()) {
      throw new BadRequestException("Usuario não informado!");
    }
    if (this.isDateRangeMissing()) {
      throw new InvalidDateRangeException("Periodo de data não informado!");
    }
    if (this.dateRange.isInvalidRange()) {
      throw new InvalidDateRangeException("Data inicial é menor que a data final!");
    }
  }

  public boolean isUserIdMissing() {
    return this.userId == null;
  }

  public boolean isDateRangeMissing() {
    return this.dateRange == null;
  }

  public PageFilter page() {
    if (page == null) {
      return new PageFilter(20, 0);
    }
    return this.page;
  }

}