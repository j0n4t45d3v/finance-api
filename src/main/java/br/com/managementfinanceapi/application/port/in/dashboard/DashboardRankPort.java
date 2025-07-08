package br.com.managementfinanceapi.application.port.in.dashboard;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardCategoryDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardTransactionDto;

import java.util.List;

public interface DashboardRankPort {
  List<DashboardTransactionDto> rankTransaction(Long userId, DateRange dateRange, int limit);
  List<DashboardCategoryDto> rankCategory(Long userId, DateRange dateRange, int limit);
}
