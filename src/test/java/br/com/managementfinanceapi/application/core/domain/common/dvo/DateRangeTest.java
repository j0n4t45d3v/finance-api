package br.com.managementfinanceapi.application.core.domain.common.dvo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {

  private static final LocalDate FIRST_DATE = LocalDate.of(1999, 1, 1);
  private static final LocalDate SECOND_DATE = LocalDate.of(2000, 1, 1);

  @Test
  @DisplayName("should verify if the date range is invalid")
  void shouldVerifyIfTheStartDateGreaterToEndDate() {
    DateRange validRangeCaseStartDateIsLessToEndDate = new DateRange(FIRST_DATE, SECOND_DATE);
    assertFalse(validRangeCaseStartDateIsLessToEndDate.isInvalidRange());

    DateRange validRangeCaseStartDateIsEqualsToEndDate = new DateRange(FIRST_DATE, FIRST_DATE);
    assertFalse(validRangeCaseStartDateIsEqualsToEndDate.isInvalidRange());

    DateRange invalidRange = new DateRange(SECOND_DATE, FIRST_DATE);
    assertTrue(invalidRange.isInvalidRange());
  }

  @Test
  @DisplayName("should verify if the start date is less to end date")
  void shouldVerifyIfTheStartDateIsLessToEndDate() {
    DateRange rangeFirstCase = new DateRange(FIRST_DATE, SECOND_DATE);
    assertTrue(rangeFirstCase.isStartDateLessToEndDate());

    DateRange rangeSecondCase = new DateRange(FIRST_DATE, FIRST_DATE);
    assertFalse(rangeSecondCase.isStartDateLessToEndDate());
  }

  @Test
  @DisplayName("should verify if the start date is equals to end date")
  void shouldVerifyIfTheStartDateIsEqualsToEndDate() {
    DateRange rangeFirstCase = new DateRange(FIRST_DATE, SECOND_DATE);
    assertFalse(rangeFirstCase.isStartDateEqualsToEndDate());

    DateRange rangeSecondCase = new DateRange(FIRST_DATE, FIRST_DATE);
    assertTrue(rangeSecondCase.isStartDateEqualsToEndDate());
  }

  @Test
  @DisplayName("should return start date with the prefix 00:00:00")
  void shouldReturnStartDateWithThePrefix_00_00_00() {
    DateRange range = new DateRange(FIRST_DATE, SECOND_DATE);
    assertEquals(FIRST_DATE.atTime(0, 0,0), range.startWithTime());
  }

  @Test
  @DisplayName("should return end date with the prefix 23:59:59")
  void shouldReturnEndDateWithThePrefix_23_59_59() {
    DateRange range = new DateRange(FIRST_DATE, SECOND_DATE);
    assertEquals(SECOND_DATE.atTime(23, 59,59), range.endWithTime());
  }
}