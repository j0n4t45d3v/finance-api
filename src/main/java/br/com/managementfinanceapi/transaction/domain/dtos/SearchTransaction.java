package br.com.managementfinanceapi.transaction.domain.dtos;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotNull;

public record SearchTransaction(
  @NotNull(message = "userId é obrigatório")
  @RequestParam(name = "userId")
  Long userId,

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
  @RequestParam(name = "startDate")
  @NotNull(message = "startDate é obrigatório")
  LocalDate startDate,

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
  @RequestParam(name = "endDate")
  @NotNull(message = "endDate é obrigatório")
  LocalDate endDate,

  Pageable pageable
) {
}