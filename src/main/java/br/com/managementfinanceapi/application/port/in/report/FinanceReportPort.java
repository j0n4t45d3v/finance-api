package br.com.managementfinanceapi.application.port.in.report;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;

public interface FinanceReportPort {

  byte[] generate(Long userId, DateRange dateRange);

}