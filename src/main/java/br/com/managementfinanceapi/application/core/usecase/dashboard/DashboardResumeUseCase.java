package br.com.managementfinanceapi.application.core.usecase.dashboard;

import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeAnnualDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeMonthlyDto;
import br.com.managementfinanceapi.application.port.in.dashboard.DashboardResumePort;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardResumeUseCase implements DashboardResumePort {

  @Override
  public DashboardResumeDto generalResume(Long userId, YearMonth yearMonth) {
    return null;
  }

  @Override
  public List<DashboardResumeMonthlyDto> resumeByMonth(Long userId, int year) {
    return List.of();
  }

  @Override
  public List<DashboardResumeAnnualDto> resumeByYear(Long userId) {
    return List.of();
  }
}
