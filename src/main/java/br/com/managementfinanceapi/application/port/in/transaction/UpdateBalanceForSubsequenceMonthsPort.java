package br.com.managementfinanceapi.application.port.in.transaction;

import java.math.BigDecimal;
import java.time.YearMonth;

public interface UpdateBalanceForSubsequenceMonthsPort {
  void execute(Long userId, BigDecimal amount, YearMonth yearMonth);
}
