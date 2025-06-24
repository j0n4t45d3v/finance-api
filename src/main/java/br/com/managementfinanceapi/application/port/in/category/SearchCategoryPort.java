package br.com.managementfinanceapi.application.port.in.category;

import java.util.List;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;

public interface SearchCategoryPort {
  List<CategoryDomain> all();
  CategoryDomain byName(Long userId, String name);
  List<CategoryTransactionSummary> getSummaryIncomeAndExpencesTotals(Long userId, DateRange dateRange);
}