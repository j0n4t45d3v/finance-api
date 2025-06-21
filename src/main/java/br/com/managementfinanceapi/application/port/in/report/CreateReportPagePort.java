package br.com.managementfinanceapi.application.port.in.report;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.report.ReportTable;

public interface CreateReportPagePort {

   ReportTable generate(Long userId, DateRange dateRange);
  
}