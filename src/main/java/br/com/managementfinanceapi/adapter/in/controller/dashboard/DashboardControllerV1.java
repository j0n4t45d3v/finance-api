package br.com.managementfinanceapi.adapter.in.controller.dashboard;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.dashboard.dto.*;
import br.com.managementfinanceapi.application.port.in.dashboard.DashboardRankPort;
import br.com.managementfinanceapi.application.port.in.dashboard.DashboardResumePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/v1/dashboards")
public class DashboardControllerV1 {

  private final DashboardResumePort dashboardResumePort;
  private final DashboardRankPort dashboardRankPort;

  public DashboardControllerV1(
      DashboardResumePort dashboardResumePort,
      DashboardRankPort dashboardRankPort
  ) {
    this.dashboardResumePort = dashboardResumePort;
    this.dashboardRankPort = dashboardRankPort;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ResponseV0<DashboardResumeDto>> resume(@PathVariable("userId") Long userId) {
    DashboardResumeDto result = dashboardResumePort.generalResume(userId, YearMonth.now());
    return ResponseEntity.ok(ResponseV0.ok(result));
  }

  @GetMapping("/rank/categories/{userId}")
  public ResponseEntity<ResponseV0<List<DashboardCategoryDto>>> rankCategories(
      @PathVariable("userId") Long userId,
      @RequestParam(value = "limit", defaultValue = "5", required = false) int limit,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    DateRange range = new DateRange(startDate, endDate);
    List<DashboardCategoryDto> result = dashboardRankPort.rankCategory(userId, range, limit);
    return ResponseEntity.ok(ResponseV0.ok(result));
  }

  @GetMapping("/rank/transactions/{userId}")
  public ResponseEntity<ResponseV0<List<DashboardTransactionDto>>> rankTransaction(
      @PathVariable("userId") Long userId,
      @RequestParam(value = "limit", defaultValue = "5", required = false) int limit,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    DateRange range = new DateRange(startDate, endDate);
    List<DashboardTransactionDto> result = dashboardRankPort.rankTransaction(userId, range, limit);
    return ResponseEntity.ok(ResponseV0.ok(result));
  }

  @GetMapping("/monthly/{userId}")
  public ResponseEntity<ResponseV0<List<DashboardResumeMonthlyDto>>> monthly(@PathVariable("userId") Long userId) {
    List<DashboardResumeMonthlyDto> result = dashboardResumePort.resumeByMonth(userId, YearMonth.now().getYear());
    return ResponseEntity.ok(ResponseV0.ok(result));
  }

  @GetMapping("/annual/{userId}")
  public ResponseEntity<ResponseV0<List<DashboardResumeAnnualDto>>> annual(@PathVariable("userId") Long userId) {
    List<DashboardResumeAnnualDto> result = dashboardResumePort.resumeByYear(userId);
    return ResponseEntity.ok(ResponseV0.ok(result));
  }
}
