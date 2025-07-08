package br.com.managementfinanceapi.application.core.usecase.dashboard;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardCategoryDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardTransactionDto;
import br.com.managementfinanceapi.application.core.domain.transaction.TransactionDomain;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import br.com.managementfinanceapi.application.port.in.dashboard.DashboardRankPort;
import br.com.managementfinanceapi.application.port.in.transaction.SearchTransactionPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DashboardRankUseCase implements DashboardRankPort {

  private final SearchTransactionPort searchTransactionPort;
  private final SearchCategoryPort searchCategoryPort;

  public DashboardRankUseCase(
      SearchTransactionPort searchTransactionPort,
      SearchCategoryPort searchCategoryPort
  ) {
    this.searchTransactionPort = searchTransactionPort;
    this.searchCategoryPort = searchCategoryPort;
  }

  @Override
  public List<DashboardTransactionDto> rankTransaction(Long userId, DateRange dateRange, int limit) {
    return this.searchTransactionPort.allByUser(userId, dateRange)
        .stream()
        .sorted(Comparator.comparing(TransactionDomain::getAmount))
        .limit(limit)
        .map(DashboardTransactionDto::of)
        .toList();
  }

  @Override
  public List<DashboardCategoryDto> rankCategory(Long userId, DateRange dateRange, int limit) {
    return List.of();
  }
}
