package br.com.managementfinanceapi.application.core.usecase.dashboard;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardCategoryDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardTransactionDto;
import br.com.managementfinanceapi.application.port.in.dashboard.DashboardRankPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardRankUseCase implements DashboardRankPort {
  
  @Override
  public List<DashboardTransactionDto> rankTransaction(Long userId, DateRange dateRange, int limit) {
    return List.of();
  }

  @Override
  public List<DashboardCategoryDto> rankCategory(Long userId, DateRange dateRange, int limit) {
    return List.of();
  }
}
