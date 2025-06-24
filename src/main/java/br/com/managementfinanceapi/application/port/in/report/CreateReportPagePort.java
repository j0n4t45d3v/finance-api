package br.com.managementfinanceapi.application.port.in.report;

import br.com.managementfinanceapi.application.core.domain.report.ReportTable;

public interface CreateReportPagePort<T> {

   ReportTable generate(T content);
  
}