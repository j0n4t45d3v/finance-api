package br.com.managementfinanceapi.application.port.in.dashboard;

import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeAnnualDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeDto;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.DashboardResumeMonthlyDto;

import java.time.YearMonth;
import java.util.List;

public interface DashboardResumePort {
  DashboardResumeDto generalResume(Long userId, YearMonth yearMonth);
  List<DashboardResumeMonthlyDto> resumeByMonth(Long userId, int year);
  List<DashboardResumeAnnualDto> resumeByYear(Long userId);
}
