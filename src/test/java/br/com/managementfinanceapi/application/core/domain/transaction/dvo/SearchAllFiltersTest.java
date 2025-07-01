package br.com.managementfinanceapi.application.core.domain.transaction.dvo;

import br.com.managementfinanceapi.application.core.domain.common.dvo.DateRange;
import br.com.managementfinanceapi.application.core.domain.common.dvo.PageFilter;
import br.com.managementfinanceapi.application.core.domain.common.exception.BadRequestException;
import br.com.managementfinanceapi.application.core.domain.common.exception.InvalidDateRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SearchAllFiltersTest {

  @Test
  @DisplayName("should throw exception when not contains user id or not contains date rage or date rage is invalid")
  void shouldThrowExceptionWhenNotContainsUserIdOrNotContainsDateRangeOrDateRageIsInvalid() {
    final LocalDate FIRST_DATE = LocalDate.of(1999, 1, 1);
    final LocalDate SECOND_DATE = LocalDate.of(2000, 1, 1);

    SearchAllFilters filtersFirstCase = new SearchAllFilters(null, null, null, null);
    SearchAllFilters filtersSecondCase = new SearchAllFilters(1L, null, null, null);
    SearchAllFilters filtersThirdCase = new SearchAllFilters(1L, new DateRange(SECOND_DATE, FIRST_DATE), null, null);

    BadRequestException thrownFirstCase = assertThrows(BadRequestException.class, filtersFirstCase::validate);
    InvalidDateRangeException thrownSecondCase = assertThrows(InvalidDateRangeException.class, filtersSecondCase::validate);
    InvalidDateRangeException thrownThirdCase = assertThrows(InvalidDateRangeException.class, filtersThirdCase::validate);

    assertEquals("Usuario não informado!", thrownFirstCase.getMessage());
    assertEquals("Periodo de data não informado!", thrownSecondCase.getMessage());
    assertEquals("Data inicial é menor que a data final!", thrownThirdCase.getMessage());
  }

  @Test
  @DisplayName("should return default pagination when page is null")
  void shouldReturnDefaultPaginationWhenPageIsNull() {
    SearchAllFilters filters = new SearchAllFilters(null, null, null, null);
    PageFilter pagination = filters.page();
    assertEquals(20, pagination.size());
    assertEquals(0, pagination.page());
  }

}