package br.com.managementfinanceapi.application.port.out.category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.core.domain.category.dvo.CategoryTransactionSummary;

public interface SearchCategoryRepositoryPort {

  List<CategoryDomain> all();
  Optional<CategoryDomain> byUserIdAndName(Long userId, String name);
  Optional<CategoryDomain> byUserIdAndId(Long userId, Long id);
  List<CategoryTransactionSummary> getSummaryIncomeAndExpencesTotals(Long userId, LocalDateTime startDate, LocalDateTime endDate);
  
}