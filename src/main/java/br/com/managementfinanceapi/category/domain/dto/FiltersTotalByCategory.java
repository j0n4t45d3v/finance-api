package br.com.managementfinanceapi.category.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

public record FiltersTotalByCategory(
    @RequestParam("month")
    @NotNull(message = "O campo 'month' é obrigatório")
    @Min(value = 1, message = "O campo 'month' não pode ser menor que 1")
    @Max(value = 12, message = "O campo 'month' não pode ser maior que 12")
    Integer month,
    @RequestParam("year")
    @NotNull(message = "O campo 'year' é obrigatório")
    Integer year,
    @RequestParam("userId")
    @NotNull(message = "O campo 'userId' é obrigatório")
    Long userId
) {
}
