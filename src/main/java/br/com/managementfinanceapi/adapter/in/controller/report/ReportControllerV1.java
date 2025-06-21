package br.com.managementfinanceapi.adapter.in.controller.report;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.port.in.report.FinanceReportPort;

@RestController
@RequestMapping(
  value="/v1/report",  
  produces = {
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    MediaType.APPLICATION_JSON_VALUE
  }
)
public class ReportControllerV1 {

  private final FinanceReportPort financeReport;

  public ReportControllerV1(FinanceReportPort financeReport) {
    this.financeReport = financeReport;
  }

  @GetMapping(value = "/resume_finance")
  public ResponseEntity<byte[]> resumeTransationReport(
    @RequestParam(name="userId", required=true) Long userId, 
    @RequestParam(name="startDate", required=true) LocalDate startDate,
    @RequestParam(name="endDate", required=true) LocalDate endDate
  ) throws IOException {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=resumo-das-transações.xlsx")
        .body(this.financeReport.generate(userId, new DateRange(startDate, endDate)));
  }

}